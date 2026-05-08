from typing import Any

from schemas import OCRItem

import numpy as np
import easyocr  # type: ignore


class OCRService:

    def __init__(self):
        self._reader: Any = easyocr.Reader(["pt"], gpu=False)

    def execute(self, image: np.ndarray) -> list[OCRItem]:
        result = self._reader.readtext(
            image,
            detail=1,
            paragraph=False,
            text_threshold=0.5,
            low_text=0.3
        )

        ocr_items: list[OCRItem] = [
            OCRItem(
                text=text,
                confidence=confidence,
                bbox=bbox
            )
            for bbox, text, confidence in result
        ]
        
        return sorted(
            ocr_items,
            key=lambda x: (min(p[1] for p in x.bbox), min(p[0] for p in x.bbox))
        )