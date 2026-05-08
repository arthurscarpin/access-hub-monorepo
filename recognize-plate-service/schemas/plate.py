from enum import Enum
from pydantic import BaseModel, Field


class PlateFormat(str, Enum):
    OLD = "ABC1234"
    MERCOSUL = "ABC1D23"

class PlateRules(BaseModel):
    formats: list[PlateFormat] = Field(
        default_factory=lambda: [
            PlateFormat.OLD,
            PlateFormat.MERCOSUL
        ]
    )
    allowed_chars: str = "A-Z0-9"