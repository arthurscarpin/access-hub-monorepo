import json
from typing import Any

from core.usecase.validate_plate_usecase import ValidatePlateUseCaseUseCase
from core.gateway.plate_gateway import PlateGateway

class ValidatePlateUseCaseUseCaseImpl(ValidatePlateUseCaseUseCase):

    def execute(
            self, 
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
        analysis_report = gateway.get_analysis_report(message)
        response = gateway.get_analysis_ai(
            model=model, 
            temperature=temperature, 
            max_tokens=max_tokens, 
            langchain_debug=langchain_debug, 
            input=json.dumps(analysis_report),
            key=key
        )

        message["finalPlate"] = response["final_plate"]
        message["finalConfidence"] = response["final_confidence"]
        message["reasoning"] = response["reasoning"]
        gateway.message_publisher(
            connection=connection, 
            exchange=exchange, 
            routing_key=routing_key, 
            message=message
        )