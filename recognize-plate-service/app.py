import re
from pathlib import Path
from typing import Any

from schemas.ocr import OCRItem
from schemas.plate import PlateRules
from schemas.request import PlateInput, PlateOutput, PlateRequest

import cv2
import numpy as np
import easyocr  # type: ignore

def set_path(filename: str) -> str:
    ROOT: Path = Path(__file__).resolve().parents[1]
    STORAGE: str = "storage"
    return str(ROOT / STORAGE / filename)

def execute_image_preprocessing(path: str) -> np.ndarray:
    image = cv2.imread(path)

    if image is None:
        raise ValueError(f"Não foi possível carregar a imagem: {path}")

    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    edges = cv2.Canny( cv2.bilateralFilter(gray, 11, 17, 17), 50, 150)
    contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    plate_crop = image

    for c in sorted(contours, key=cv2.contourArea, reverse=True)[:15]:
        approx = cv2.approxPolyDP(c, 0.02 * cv2.arcLength(c, True), True)

        if len(approx) == 4:
            x, y, w, h = cv2.boundingRect(approx)
            aspect_ratio = w / float(h)

            if 2.2 <= aspect_ratio <= 5.5 and w > 80 and h > 25:
                plate_crop = image[y:y + h, x:x + w]
                break

    processed = cv2.cvtColor(plate_crop, cv2.COLOR_BGR2GRAY)
    processed = cv2.resize(processed, None, fx=2, fy=2, interpolation=cv2.INTER_CUBIC)
    processed = cv2.bilateralFilter(processed, 9, 75, 75)
    return cv2.createCLAHE(2.0, (8, 8)).apply(processed)

def execute_ocr_processing(pre_processed_image: np.ndarray) -> list[OCRItem]:
    reader: Any = easyocr.Reader(["pt"], gpu=False)
    result: list[Any] = reader.readtext(
        pre_processed_image,
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

def normalize_plate(ocr_items: list[OCRItem]) -> str:
    raw_text: str = "".join([item.text for item in ocr_items])
    raw_text = raw_text.upper()
    raw_text = re.sub(r"[^A-Z0-9]", "", raw_text)
    return (raw_text.replace("O", "0").replace("I", "1").replace("B", "8"))

def is_valid_plate(text: str) -> bool:
    old = re.fullmatch(r"[A-Z]{3}[0-9]{4}", text)
    new = re.fullmatch(r"[A-Z]{3}[0-9][A-Z][0-9]{2}", text)
    return old is not None or new is not None

if __name__ == "__main__":
    filename: str = "test.webp"
    path: str = set_path(filename)

    pre_processed_image = execute_image_preprocessing(path)
    ocr_processed = execute_ocr_processing(pre_processed_image)
    raw_text = normalize_plate(ocr_processed)

    prompt: str = (
        "Corrija o OCR usando apenas os fragmentos fornecidos. "
        "Remova ruídos e retorne somente uma placa válida brasileira. "
        "Se não for possível, retorne null."
    )

    payload = PlateRequest(
        input=PlateInput(
            raw=raw_text,
            items=[
            OCRItem(
                text=item.text,
                confidence=float(item.confidence),
                bbox=item.bbox
            ) 
            for item in ocr_processed
            ]
        ),
        rules=PlateRules(),
        instruction="" if is_valid_plate(raw_text) else prompt,
        output=PlateOutput()
    )
    print(payload.model_dump_json(indent=2, ensure_ascii=False))