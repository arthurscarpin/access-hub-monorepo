from typing import Any

from abc import ABC, abstractmethod

class CaptureGateway(ABC):

    @abstractmethod
    def message_publisher(self, message: dict[str, str], connection: Any, exchange: str, routing_key: str):
        pass

    @abstractmethod
    def storage_build_path(self, filename: str, storage: str) -> str:
        pass

    @abstractmethod
    def image_preprocessor(self, storage_path: str) -> Any:
        pass

    @abstractmethod
    def ocr_processor(self, image: Any) -> list[dict[str, Any]]:
        pass

    @abstractmethod
    def plate_normalize(self, plate: str) -> str:
        pass

    @abstractmethod
    def plate_is_valid(self, plate: str) -> bool:
        pass