import pytest
from infrastructure.plate_analysis.plate_candidate_aggregator import PlateCandidateAggregator

class TestPlateCandidateAggregator:

    @pytest.fixture
    def aggregator(self):
        return PlateCandidateAggregator()

    def test_analyse_mercosul_format(self, aggregator: PlateCandidateAggregator):
        item = {"text": "abc-1d23", "confidence": 0.9}
        result = aggregator._analyse(item)
        
        assert result["text"] == "ABC1D23"
        assert result["is_valid_format"] is True
        assert result["confidence"] == 0.9

    def test_analyse_old_format(self, aggregator: PlateCandidateAggregator):
        item = {"text": "ABC 1234", "confidence": 0.855555}
        result = aggregator._analyse(item)
        
        assert result["text"] == "ABC1234"
        assert result["is_valid_format"] is True
        assert result["confidence"] == 0.8556

    def test_analyse_invalid_format(self, aggregator: PlateCandidateAggregator):
        item = {"text": "123-ABCD", "confidence": 0.5}
        result = aggregator._analyse(item)
        
        assert result["is_valid_format"] is False

    def test_add_to_report_aggregation(self, aggregator: PlateCandidateAggregator):
        aggregator.add_to_report({"text": "ABC1234", "confidence": 0.7})
        aggregator.add_to_report({"text": "abc1234", "confidence": 0.9})
        
        report = aggregator.get_final_report()
        assert len(report) == 1
        assert report[0]["count"] == 2
        assert report[0]["max_confidence"] == 0.9

    def test_add_to_report_ignore_short_text(self, aggregator: PlateCandidateAggregator):
        aggregator.add_to_report({"text": "AB", "confidence": 0.9})
        assert len(aggregator.candidates) == 0

    def test_get_final_report_sorting_logic(self, aggregator: PlateCandidateAggregator):
        aggregator.add_to_report({"text": "INV123", "confidence": 0.9})
        aggregator.add_to_report({"text": "INV123", "confidence": 0.9})
        
        aggregator.add_to_report({"text": "ABC1234", "confidence": 0.5})

        aggregator.add_to_report({"text": "XYZ1234", "confidence": 0.8})

        report = aggregator.get_final_report()

        assert report[0]["text"] == "XYZ1234"
        assert report[1]["text"] == "ABC1234"
        assert report[2]["text"] == "INV123"

    def test_normalization_removes_junk(self, aggregator: PlateCandidateAggregator):
        item = {"text": "A!B@C-1.2/3#4", "confidence": 1.0}
        result = aggregator._analyse(item)
        assert result["text"] == "ABC1234"