from abc import ABC, abstractmethod
from typing import Any

from core.gateway.capture_gateway import CaptureGateway


class CaptureUseCase(ABC):

    @abstractmethod
    def execute(self, 
                message: dict[str, str], 
                gateway: CaptureGateway, 
                connection: Any, 
                exchange: str, 
                routing_key: str,
                logger: Any) -> dict[str, Any]:
        pass