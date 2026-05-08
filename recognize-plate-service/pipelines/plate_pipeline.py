from configuration import StorageConfig
from service import OCRService, PlateService, PreProcessorImageService
from schemas import OCRItem, PlateRules, PlateInput, PlateOutput, PlateRequest

class PlatePipeline:
    
    def __init__(
        self, 
        storage: StorageConfig, 
        image_service: PreProcessorImageService,
        ocr_service: OCRService,
        plate_service: PlateService
    ):
        self._storage = storage
        self._image_service = image_service
        self._ocr_service = ocr_service
        self._plate_service = plate_service

    def run(self, filename: str) -> PlateRequest:
        path = self._storage.build(filename)
        image = self._image_service.execute(path=path)
        ocr_processed = self._ocr_service.execute(image=image)
        normalized = self._plate_service.normalize(items=ocr_processed)
        instruction = None if self._plate_service.is_valid(normalized) else ""

        request = PlateRequest(
            input=PlateInput(
                raw=normalized,
                items=[
                    OCRItem(
                        text=i.text,
                        confidence=float(i.confidence),
                        bbox=i.bbox
                    )
                    for i in ocr_processed
                ]
            ),
            rules=PlateRules(),
            instruction=instruction,
            output=PlateOutput()
        )
        return request