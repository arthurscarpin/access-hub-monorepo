from typing import List, Literal, Optional
from schemas.ocr import OCRItem
from schemas.plate import PlateRules
from pydantic import BaseModel, Field


class PlateInput(BaseModel):
    raw: str
    items: List[OCRItem]

class PlateOutput(BaseModel):
    plate: Optional[str] = None
    valid: bool = False
    confidence: float = Field(default=0.0, ge=0.0, le=1.0)

class PlateRequest(BaseModel):
    task: Literal["license_plate_correction"] = "license_plate_correction"
    country: Literal["BR"] = "BR"
    input: PlateInput
    rules: PlateRules
    instruction: str
    output: PlateOutput