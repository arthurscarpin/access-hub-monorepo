from pydantic import BaseModel, Field

class PlateOutputParser(BaseModel):
    final_plate: str = Field(description="The corrected and final plate")
    final_confidence: float = Field(description="The weighted average confidence level, or the confidence level calculated for this decision")
    reasoning: str = Field(description="Brief explanation of the correction made")