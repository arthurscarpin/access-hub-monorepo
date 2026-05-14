import re
from typing import Any

class PlateCandidateAggregator:

    def __init__(self):
        self.pattern_mercosul = re.compile(r'^[A-Z]{3}[0-9][A-Z][0-9]{2}$')
        self.pattern_old = re.compile(r'^[A-Z]{3}[0-9]{4}$')
        self.candidates: dict[str, Any] = {}

    def _analyse(self, ocr_item: dict[str, Any]) -> dict[str, Any]:
        raw_text = ocr_item.get("text", "")
        confidence = ocr_item.get("confidence", 0.0)
        
        norm_text = "".join(re.findall(r'[A-Z0-9]', raw_text.upper()))
        
        is_valid = bool(self.pattern_mercosul.match(norm_text) or self.pattern_old.match(norm_text))
        
        return {
            "text": norm_text, 
            "confidence": round(confidence, 4), 
            "is_valid_format": is_valid, 
            "original_text": raw_text
        }

    def add_to_report(self, ocr_item: dict[str, Any]):
        analysis = self._analyse(ocr_item)
        text = analysis["text"]
        
        if len(text) < 3: 
            return
        
        if text not in self.candidates:
            self.candidates[text] = {
                "text": text,
                "count": 0,
                "max_confidence": 0.0,
                "is_valid_format": analysis["is_valid_format"]
            }
        
        self.candidates[text]["count"] += 1
        
        if analysis["confidence"] > self.candidates[text]["max_confidence"]:
            self.candidates[text]["max_confidence"] = analysis["confidence"]

    def get_final_report(self) -> list[dict[str, Any]]:
        all_candidates = list(self.candidates.values())
        
        return sorted(
            all_candidates, 
            key=lambda x: (x['is_valid_format'], x['count'], x['max_confidence']), 
            reverse=True
        )