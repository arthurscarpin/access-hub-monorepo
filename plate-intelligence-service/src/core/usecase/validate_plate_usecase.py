from abc import ABC, abstractmethod
from typing import Any

from core.gateway.plate_gateway import PlateGateway

class ValidatePlateUseCaseUseCase(ABC):

    @abstractmethod
    def execute(self, 
                message: dict[str, Any], 
                gateway: PlateGateway,
                model: str,
                connection: Any, 
                exchange: str, 
                routing_key: str,
                logger: Any,
                temperature: float,
                max_tokens: int,
                langchain_debug: bool,
                key: str):
        pass