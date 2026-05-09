from typing import Any
import numpy as np
import easyocr


class OCRService:

    def __init__(self) -> None:
        self._reader: Any = easyocr.Reader(["pt"], gpu=True)

    def execute(self, image: np.ndarray) -> list[dict[str, Any]]:
        if image is None:
            raise ValueError("Image cannot be None")

        result = self._reader.readtext(
            image,
            detail=1,
            paragraph=False,
            text_threshold=0.5,
            low_text=0.3,
        )

        ocr_items: list[dict[str, Any]] = []

        for bbox, text, confidence in result:
            if not text:
                continue

            ocr_items.append(
                {
                    "text": text.strip(),
                    "confidence": float(confidence),
                    "bbox": bbox,
                }
            )

        return sorted(
            ocr_items,
            key=lambda x: (
                min(p[1] for p in x["bbox"]),
                min(p[0] for p in x["bbox"]),
            ),
        )