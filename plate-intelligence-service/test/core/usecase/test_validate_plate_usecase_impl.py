import pytest
import json
from unittest.mock import MagicMock
from core.usecase.validate_plate_usecase_impl import ValidatePlateUseCaseUseCaseImpl

class TestValidatePlateUseCase:

    @pytest.fixture
    def mock_gateway(self):
        return MagicMock()

    @pytest.fixture
    def mock_logger(self):
        return MagicMock()

    @pytest.fixture
    def use_case(self):
        return ValidatePlateUseCaseUseCaseImpl()

    @pytest.fixture
    def default_params(self, mock_gateway: MagicMock, mock_logger: MagicMock) -> dict[str, str]:
        return {
            "message": {"id": "123", "data": "test"},
            "gateway": mock_gateway,
            "model": "gpt-4",
            "connection": MagicMock(),
            "exchange": "test_exchange",
            "routing_key": "test_key",
            "logger": mock_logger,
            "temperature": 0.0,
            "max_tokens": 100,
            "langchain_debug": False,
            "key": "api-key"
        }

    def test_execute_success(self, 
                             use_case: ValidatePlateUseCaseUseCaseImpl, 
                             default_params: dict[str, str], 
                             mock_gateway: MagicMock):
        mock_gateway.get_analysis_report.return_value = {"report": "ok"}
        mock_gateway.get_analysis_ai.return_value = {
            "final_plate": "ABC1234",
            "final_confidence": 0.98,
            "reasoning": "Claro e visível"
        }

        use_case.execute(**default_params)

        msg = default_params["message"]
        assert msg["status"] == "COMPLETED"
        assert msg["finalPlate"] == "ABC1234"
        assert msg["finalConfidence"] == 0.98
        mock_gateway.message_publisher.assert_called_once()
        args, kwargs = mock_gateway.message_publisher.call_args
        assert kwargs["message"]["capture"]["status"] == "COMPLETED"
        assert kwargs["message"]["error"] is None

    def test_execute_exception_handling(self, 
                                        use_case: ValidatePlateUseCaseUseCaseImpl, 
                                        default_params: dict[str, str], 
                                        mock_gateway: MagicMock):
        mock_gateway.get_analysis_report.side_effect = Exception("Erro de conexão")

        use_case.execute(**default_params)

        msg = default_params["message"]
        assert msg["status"] == "FAILED"
        mock_gateway.message_publisher.assert_called_once()
        args, kwargs = mock_gateway.message_publisher.call_args
        assert kwargs["message"]["capture"]["status"] == "FAILED"
        assert "Erro de conexão" in kwargs["message"]["error"]
        default_params["logger"].error.assert_called()

    def test_execute_missing_id_in_message(self, 
                                            use_case: ValidatePlateUseCaseUseCaseImpl, 
                                            default_params: dict[str, str], 
                                            mock_gateway: MagicMock):
        default_params["message"] = {"data": "no-id"}
        mock_gateway.get_analysis_report.return_value = {}
        mock_gateway.get_analysis_ai.return_value = {
            "final_plate": "XYZ9999", "final_confidence": 1.0, "reasoning": "N/A"
        }

        use_case.execute(**default_params)

        info_calls = [call.args[0] for call in default_params["logger"].info.call_args_list]
        assert any("unknown" in call for call in info_calls)

    def test_json_serialization_of_input(self, 
                                         use_case: ValidatePlateUseCaseUseCaseImpl, 
                                         default_params: dict[str, str], 
                                         mock_gateway: MagicMock):
        report_data = {"raw": "data"}
        mock_gateway.get_analysis_report.return_value = report_data
        
        use_case.execute(**default_params)
        
        mock_gateway.get_analysis_ai.assert_called_once()
        sent_input = mock_gateway.get_analysis_ai.call_args.kwargs["input"]
        assert sent_input == json.dumps(report_data)