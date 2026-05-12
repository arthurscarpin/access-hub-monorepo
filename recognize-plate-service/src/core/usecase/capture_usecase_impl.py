from typing import Any

from core.usecase.capture_usecase import CaptureUseCase
from core.gateway.capture_gateway import CaptureGateway
from core.domain.capture_status import CaptureStatus


class CaptureUseCaseImpl(CaptureUseCase):

    def execute(self, 
                message: dict[str, str], 
                gateway: CaptureGateway, 
                connection: Any, 
                exchange: str, 
                routing_key: str,
                logger: Any,
                storage: str) -> dict[str, Any]:
        filename = message.get("filename", "")
        message_producer = {}
        
        if filename == "":
            message_producer = {
                **message, 
                "status": CaptureStatus.FAILED.value, 
                "message": "Filename is not found",
                "ocr": []
            }
            gateway.message_publisher(
                message=message_producer, 
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key
            )
            logger.error("Filename is not found")
            return message_producer

        try:
            logger.info("Stage 1 - Execution started")
            message_producer = {
                **message, 
                "status": CaptureStatus.STARTED.value, 
                "message": "Execution started",
                "ocr": []
            }
            gateway.message_publisher(
                message=message_producer, 
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key
            )

            logger.info(f"File {filename=} found in storage")
            storage_path = gateway.storage_build_path(filename, storage)

            logger.info("Stage 2 - Execution started")
            message_producer = {
                **message, 
                "status": CaptureStatus.PROCESSING.value, 
                "message": "Execution in processing...",
                "ocr": []
            }
            gateway.message_publisher(
                message=message_producer, 
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key
            )
            
            pre_processed_image = gateway.image_preprocessor(storage_path=storage_path)
            logger.debug("The image has been pre-processed")

            ocr_image = gateway.ocr_processor(pre_processed_image)
            logger.debug("The OCR has been pre-processed")
            logger.debug(ocr_image)
            logger.info("OCR Extracted")

            message_producer: dict[str, Any] = {
                **message, 
                "status": CaptureStatus.COMPLETED.value, 
                "message": "Execution completed",
                "ocr": ocr_image
            }
            gateway.message_publisher(
                message=message_producer, 
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key
            )
            logger.info("Stage 3 - Execution completed")
            return message_producer
        except Exception as e:
            message_producer = {
                **message, 
                "status": CaptureStatus.FAILED.value, 
                "message": e, 
                "ocr": []
            }
            gateway.message_publisher(
                message=message_producer, 
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key
            )
            logger.error(f"Error {type(e).__name__}: {str(e)}")
            return message_producer