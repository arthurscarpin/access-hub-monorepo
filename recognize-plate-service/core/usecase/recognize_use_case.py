from typing import Any

from core import logger


class RecognizeUseCase:

    def __init__(
        self,
        storage,
        image_service,
        ocr_service,
        plate_service,
        event_publisher,
    ):
        self._storage = storage
        self._image_service = image_service
        self._ocr_service = ocr_service
        self._plate_service = plate_service
        self._events = event_publisher

        logger.info("ocr_pipeline.initialized")

    def run(self, consumer_message: dict[str, Any]) -> None:
        filename = consumer_message.get("filename")

        if not filename:
            self._events.failed({
                **consumer_message,
                "status": "FAILED",
                "error": "missing_filename",
            })
            logger.error("ocr_pipeline.invalid_message filename_missing")
            raise ValueError("Filename is required")

        pipeline_ctx = f"file={filename}"

        try:
            logger.info(f"ocr_pipeline.started {pipeline_ctx}")

            self._events.started({
                **consumer_message,
                "stage": "started",
            })

            path = self._storage.build(filename)
            logger.info(f"ocr_pipeline.storage_resolved {pipeline_ctx} path={path}")

            self._events.processing({
                **consumer_message,
                "stage": "processing",
            })

            image = self._image_service.execute(path=path)
            logger.info(f"ocr_pipeline.image_processed {pipeline_ctx}")

            ocr_processed = self._ocr_service.execute(image=image)
            logger.info(f"ocr_pipeline.ocr_done {pipeline_ctx} items={len(ocr_processed)}")

            normalized = self._plate_service.normalize(items=ocr_processed)
            is_valid = self._plate_service.is_valid(normalized)

            logger.info(
                f"ocr_pipeline.completed {pipeline_ctx} plate={normalized} valid={is_valid}"
            )

            self._events.completed({
                **consumer_message,
                "plate": normalized,
                "is_valid": is_valid,
                "stage": "completed",
            })

        except Exception as e:
            logger.error(
                f"ocr_pipeline.failed {pipeline_ctx} error={type(e).__name__}: {e}",
                exc_info=True,
            )

            self._events.failed({
                **consumer_message,
                "stage": "failed",
                "error": str(e),
            })

            raise