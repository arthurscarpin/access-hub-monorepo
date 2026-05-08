from typing import List
from pydantic import BaseModel, Field


class OCRItem(BaseModel):
    text: str
    confidence: float = Field(ge=0.0, le=1.0)
    bbox: List[List[int]]