import json
from typing import Any

from core.plate.usecase.validate_plate_usecase import ValidatePlateUseCaseUseCase
from core.plate.gateway.plate_gateway import PlateGateway

class ValidatePlateUseCaseUseCaseImpl(ValidatePlateUseCaseUseCase):

    def execute(self, 
                message: dict[str, Any], 
                gateway: PlateGateway,
                model: str,
                temperature: float,
                max_tokens: int,
                langchain_debug: bool):
        analysis_report = gateway.get_analysis_report(message)
        response = gateway.get_analysis_ai(
            model=model, 
            temperature=temperature, 
            max_tokens=max_tokens, 
            langchain_debug=langchain_debug, 
            input=json.dumps(analysis_report)
        )
        print(response)