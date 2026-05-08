from configuration import StorageConfig
from service import OCRService, PlateService, PreProcessorImageService
from schemas import OCRItem, PlateRules, PlateInput, PlateOutput, PlateRequest

from configuration import logger


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

        logger.info("PlatePipeline initialized")

    def run(self, filename: str) -> PlateRequest:
        try:
            logger.info(f"Starting pipeline for file: {filename}")

            # 1. Path resolve
            path = self._storage.build(filename)
            logger.debug(f"Resolved image path: {path}")

            # 2. Load image
            logger.info("Loading image...")
            image = self._image_service.execute(path=path)
            logger.debug(f"Image loaded successfully: {type(image)}")

            # 3. OCR
            logger.info("Running OCR service...")
            ocr_processed = self._ocr_service.execute(image=image)
            logger.debug(f"OCR items detected: {len(ocr_processed)}")

            # 4. Normalize plate
            logger.info("Normalizing plate data...")
            normalized = self._plate_service.normalize(items=ocr_processed)
            logger.debug(f"Normalized result: {normalized}")

            # 5. Validation
            is_valid = self._plate_service.is_valid(normalized)
            instruction = None if is_valid else ""
            logger.info(f"Plate valid: {is_valid}")

            # 6. Build request
            logger.info("Building PlateRequest object...")

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

            logger.info("Pipeline completed successfully")

            return request

        except Exception as e:
            logger.error(f"Pipeline failed for file {filename}: {str(e)}", exc_info=True)
            raise