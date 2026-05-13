from abc import ABC, abstractmethod
from typing import Any

from core.plate.gateway.plate_gateway import PlateGateway

class ValidatePlateUseCaseUseCase(ABC):

    @abstractmethod
    def execute(self, message: dict[str, Any], gateway: PlateGateway):
        pass