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
        
        capture_id = message.get("id", "unknown")
        logger.info(f"Starting plate validation use case for capture ID: {capture_id}")

        try:
            analysis_report = gateway.get_analysis_report(message)
            logger.debug(f"Analysis report generated for {capture_id}: {analysis_report}")

            logger.info(f"Requesting AI analysis using model: {model}")
            ai_input = json.dumps(analysis_report)
            logger.debug(f"AI Input payload: {ai_input}")

            response = gateway.get_analysis_ai(
                model=model, 
                temperature=temperature, 
                max_tokens=max_tokens, 
                langchain_debug=langchain_debug, 
                input=ai_input,
                key=key
            )
            
            logger.debug(f"AI Raw response: {response}")
            logger.info(f"AI reconstruction successful for {capture_id}: {response.get('final_plate')}")

            message["finalPlate"] = response["final_plate"]
            message["finalConfidence"] = response["final_confidence"]
            message["reasoning"] = response["reasoning"]
            message["status"] = "COMPLETED"

            output_payload: dict[str, Any] = {"capture": message, "error": None}
            logger.debug(f"Publishing success message: {output_payload}")

            gateway.message_publisher(
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key, 
                message=output_payload
            )
            logger.info(f"Validation completed and published for ID: {capture_id}")

        except Exception as e:
            logger.error(f"Error in ValidatePlateUseCase for ID {capture_id}: {str(e)}", exc_info=True)
            message["status"] = "FAILED"
            error_payload: dict[str, Any] = {"capture": message, "error": str(e)}
            logger.debug(f"Publishing failure message: {error_payload}")
            gateway.message_publisher(
                connection=connection, 
                exchange=exchange, 
                routing_key=routing_key, 
                message=error_payload
            )