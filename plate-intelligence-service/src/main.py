from typing import Any

from core import ValidatePlateUseCaseUseCaseImpl
from infrastructure import PlateAnlysisGateway, get_settings

message: dict[str,Any] = {'id': '6a03866da5fc100cf9d6028b', 'images': [{'id': '2fec72e1-4109-49b3-a001-7c3edd07c351', 'filename': 'car1.webp', 'status': 'COMPLETED', 'ocr': [{'text': 'HAT', 'confidence': 0.48065905256943753, 'bbox': [[710, 534], [774, 534], [774, 588], [710, 588]]}, {'text': 'R102a19?', 'confidence': 0.29243414907131077, 'bbox': [[601, 669], [889, 669], [889, 745], [601, 745]]}], 'timestamp': '2026-05-12T19:58:56.598217269Z'}, {'id': '2fec72e1-4109-49b3-a001-7c3edd07c351', 'filename': 'car1.webp', 'status': 'COMPLETED', 'ocr': [{'text': 'HAT', 'confidence': 0.48065905256943753, 'bbox': [[710, 534], [774, 534], [774, 588], [710, 588]]}, {'text': 'R102a19?', 'confidence': 0.29243414907131077, 'bbox': [[601, 669], [889, 669], [889, 745], [601, 745]]}], 'timestamp': '2026-05-12T19:58:56.598217269Z'}], 'status': 'PROCESSING', 'direction': 'entry', 'finalPlate': None, 'finalConfidence': None, 'createdAt': '2026-05-12T19:58:37.138Z', 'updatedAt': '2026-05-12T19:58:56.598219845Z', 'processedAt': None, 'processedImagesCount': 1, 'version': None}

def main():
    settings = get_settings()
    gateway = PlateAnlysisGateway()
    use_case = ValidatePlateUseCaseUseCaseImpl()
    use_case.execute(
        message=message, 
        gateway=gateway, 
        model=settings.MODEL, 
        temperature=settings.TEMPERATURE, 
        max_tokens=settings.MAX_TOKENS, 
        langchain_debug=settings.LANGCHAIN_DEBUG
    )

if __name__ == "__main__":
    main()