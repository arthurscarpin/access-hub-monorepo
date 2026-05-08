from functools import wraps
from pathlib import Path
from typing import Callable, Any

from pipelines import PlatePipeline
from configuration import StorageConfig
from service import OCRService, PlateService, PreProcessorImageService


def inject_plate_pipeline(fn: Callable[..., Any]):

    @wraps(fn)
    def wrapper(*args: Any, **kwargs: Any):
        storage = StorageConfig(
            root=Path(__file__).resolve().parents[2],
            folder="storage"
        )

        pipeline = PlatePipeline(
            storage=storage,
            image_service=PreProcessorImageService(),
            ocr_service=OCRService(),
            plate_service=PlateService()
        )

        return fn(pipeline, *args, **kwargs)

    return wrapper