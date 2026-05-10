import re

class Plate:

    @staticmethod
    def normalize(plate: str) -> str:
        raw_text = plate
        raw_text = raw_text.upper()
        raw_text = re.sub(r"[^A-Z0-9]", "", raw_text)
        return raw_text.replace("O", "0").replace("I", "1").replace("B", "8")

    @staticmethod
    def is_valid(plate: str) -> bool:
        old = re.fullmatch(r"[A-Z]{3}[0-9]{4}", plate)
        mercosul = re.fullmatch(r"[A-Z]{3}[0-9][A-Z][0-9]{2}", plate)
        return old is not None or mercosul is not None