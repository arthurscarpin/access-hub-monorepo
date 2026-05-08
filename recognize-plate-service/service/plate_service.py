import re

from schemas import OCRItem


class PlateService:

    def normalize(self, items: list[OCRItem]) -> str:
        raw_text: str = "".join([item.text for item in items])
        raw_text = raw_text.upper()
        raw_text = re.sub(r"[^A-Z0-9]", "", raw_text)
        return (raw_text.replace("O", "0").replace("I", "1").replace("B", "8"))

    def is_valid(self, plate: str) -> bool:
        old = re.fullmatch(r"[A-Z]{3}[0-9]{4}", plate)
        new = re.fullmatch(r"[A-Z]{3}[0-9][A-Z][0-9]{2}", plate)
        return old is not None or new is not None