from typing import Any

from core import PlateGateway
from infrastructure.plate_analysis.plate_candidate_aggregator import PlateCandidateAggregator

class PlateAnlysisGateway(PlateGateway):

    def get_analysis_report(self, message: dict[str, Any]) -> list[dict[str, Any]]:
        plate_aggregator = PlateCandidateAggregator()
        images = message.get("images", [])
        for image in images:
            ocr_list = image.get("ocr", [])
            for ocr_item in ocr_list:
                plate_aggregator.add_to_report(ocr_item)
        return plate_aggregator.get_final_report()