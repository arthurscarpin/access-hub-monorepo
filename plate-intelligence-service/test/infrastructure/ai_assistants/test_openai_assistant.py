import pytest
from unittest.mock import MagicMock, patch
from infrastructure.ai_assistants.openai_assistant import OpenAIAssistant

class TestOpenAIAssistant:

    @pytest.fixture
    def components(self) -> dict[str, str]:
        return {
            "llm": MagicMock(),
            "parser": MagicMock(),
            "prompt_template": MagicMock(),
            "assistant_name": "PlateAssistant",
            "tags": ["vision"]
        }

    def test_init_configures_debug(self, components: dict[str, str]):
        with patch("infrastructure.ai_assistants.openai_assistant.set_debug") as mock_debug:
            OpenAIAssistant(**components, debug=True)
            mock_debug.assert_called_once_with(True)

    def test_execute_chain_invocation(self, components: dict[str, str]):
        components["parser"].get_format_instructions.return_value = "JSON_FORMAT"
        assistant = OpenAIAssistant(**components)
        
        mock_result = {"plate": "ABC"}
        assistant._chain.invoke = MagicMock(return_value=mock_result)

        result = assistant.execute("analyze this")

        assistant._chain.invoke.assert_called_once_with(
            {"input": "analyze this", "instructions": "JSON_FORMAT"},
            config={"run_name": "PlateAssistant", "tags": ["vision"]}
        )
        assert result == mock_result

    def test_execute_exception_propagation(self, components: dict[str, str]):
        assistant = OpenAIAssistant(**components)
        assistant._chain.invoke = MagicMock(side_effect=ValueError("Invalid Input"))

        with pytest.raises(ValueError, match="Invalid Input"):
            assistant.execute("fail")