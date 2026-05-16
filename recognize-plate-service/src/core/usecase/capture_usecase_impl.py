from typing import Any
from core.usecase.capture_usecase import CaptureUseCase
from core.gateway.capture_gateway import CaptureGateway
from core.domain.status import ImageStatus, CaptureStatus


class CaptureUseCaseImpl(CaptureUseCase):

    def execute(
            self,
            message: dict[str, str],
            gateway: CaptureGateway,
            connection: Any,
            exchange: str,
            routing_key: str,
            logger: Any,
            storage: str) -> dict[str, Any]:

        filename = message.get("filename", "")

        if filename == "":
            message_producer = {
                **message,
                "image_status": ImageStatus.FAILED.value,
                "capture_status": CaptureStatus.PROCESSING.value,
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
                "image_status": ImageStatus.STARTED.value,
                "capture_status": CaptureStatus.PROCESSING.value,
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
                "image_status": ImageStatus.PROCESSING.value,
                "capture_status": CaptureStatus.PROCESSING.value,
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
                "image_status": ImageStatus.COMPLETED.value,
                "capture_status": CaptureStatus.PROCESSING.value,
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
            if self._is_retryable(e):
                logger.warning(
                    f"Transient error in CaptureUseCase for file {filename}, "
                    f"will retry: {type(e).__name__}: {str(e)}"
                )
                raise

            logger.error(f"Error {type(e).__name__}: {str(e)}")
            message_producer = {
                **message,
                "image_status": ImageStatus.FAILED.value,
                "capture_status": CaptureStatus.PROCESSING.value,
                "message": str(e),
                "ocr": []
            }
            gateway.message_publisher(
                message=message_producer,
                connection=connection,
                exchange=exchange,
                routing_key=routing_key
            )
            return message_producer

    def _is_retryable(self, error: Exception) -> bool:
        non_retryable = (
            FileNotFoundError,
            ValueError,
            TypeError,
        )
        if isinstance(error, non_retryable):
            return False

        retryable_hints = [
            "timeout",
            "connection",
            "unavailable",
            "memory",
            "resource",
        ]
        return any(hint in str(error).lower() for hint in retryable_hints)