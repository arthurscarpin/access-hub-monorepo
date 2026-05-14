from typing import Any

from core import PlateGateway
from infrastructure.plate_analysis.plate_candidate_aggregator import PlateCandidateAggregator
from infrastructure.parsers.output_parsers import PlateOutputParser
from infrastructure.configuration.chat_openai_config import ChatOpenAIConfig
from infrastructure.ai_assistants.openai_assistant import OpenAIAssistant
from infrastructure.producer.plate_result_producer import PlateResultProducer

class PlateAnlysisGateway(PlateGateway):

    def get_analysis_report(self, message: dict[str, Any]) -> list[dict[str, Any]]:
        plate_aggregator = PlateCandidateAggregator()
        images = message.get("images", [])
        for image in images:
            ocr_list = image.get("ocr", [])
            for ocr_item in ocr_list:
                plate_aggregator.add_to_report(ocr_item)
        return plate_aggregator.get_final_report()
    
    def get_analysis_ai(self, model: str, temperature: float, max_tokens: int, langchain_debug: bool, input: str, key: str) -> dict[str, Any]:
        chat_openai_config = (
            ChatOpenAIConfig(model=model, temperature=temperature, max_tokens=max_tokens, key=key)
                .set_prompt_template(human_prompt="OCR batch: {input}", system_prompt_filename="argo_assistant_system_prompt")
                .set_parser(output_parser=PlateOutputParser)
        )
        argus_assistant = OpenAIAssistant(
            llm=chat_openai_config.llm,
            parser=chat_openai_config.parser, # type: ignore
            prompt_template=chat_openai_config.prompt_template, # type: ignore
            assistant_name="Argus--Plate-Reconstructor-Assistant",
            tags=["v1-release", "plate-service"],
            debug=langchain_debug
        )
        return argus_assistant.execute(input)
    
    def message_publisher(self, message: dict[str, str], connection: Any, exchange: str, routing_key: str):
        producer = PlateResultProducer(connection=connection, exchange=exchange)
        producer.publish(routing_key=routing_key, payload=message)