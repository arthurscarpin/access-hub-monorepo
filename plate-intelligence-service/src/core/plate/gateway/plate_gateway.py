from abc import ABC, abstractmethod
from typing import Any

class PlateGateway(ABC):

    @abstractmethod
    def get_analysis_report(self, message: dict[str, Any]) -> list[dict[str, Any]]:
        pass