from typing import Any

from core import CaptureGateway, Storage
from infrastructure.image_processing.opencv_preprocessor import OpenCVPreProcessor
from infrastructure.ocr.easyocr_processor import EasyOCRProcessor
from infrastructure.producer.ocr_status_producer import OCRStatusProducer

import numpy

class CapturePublisherGateway(CaptureGateway):

    def message_publisher(self, message: dict[str, str], connection: Any, exchange: str, routing_key: str):
        producer = OCRStatusProducer(connection=connection, exchange=exchange)
        producer.publish(routing_key=routing_key, payload=message)

    def storage_build_path(self, filename: str, storage: str) -> str:
        storage = Storage(storage=storage) \
            .build(filename=filename)
        return storage
    
    def image_preprocessor(self, storage_path: str) -> numpy.ndarray:
        pre_processor = OpenCVPreProcessor()
        return pre_processor.execute(path=storage_path)
    
    def ocr_processor(self, image: Any) -> list[dict[str, Any]]:
        ocr = EasyOCRProcessor()
        return ocr.execute(image=image)