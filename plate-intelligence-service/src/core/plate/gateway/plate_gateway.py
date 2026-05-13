from abc import ABC, abstractmethod
from typing import Any

class PlateGateway(ABC):

    @abstractmethod
    def get_analysis_report(self, message: dict[str, Any]) -> list[dict[str, Any]]:
        pass
    
    @abstractmethod
    def get_analysis_ai(self, model: str, temperature: float, max_tokens: int, langchain_debug: bool, input: str) -> dict[str, Any]:
        pass