from typing import Any

from langchain_openai import ChatOpenAI
from langchain_core.output_parsers import JsonOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnableSerializable
from langchain_core.globals import set_debug

class OpenAIAssistant:

    def __init__(
            self, 
            llm: ChatOpenAI, 
            parser: JsonOutputParser, 
            prompt_template: ChatPromptTemplate,
            assistant_name: str,
            tags: list[str],
            debug: bool=False):
        self._llm = llm
        self._parser = parser
        self._prompt_template = prompt_template
        self._chain: RunnableSerializable[dict[str, Any], Any] = self._prompt_template | self._llm | self._parser
        self._assistant_name = assistant_name
        self._tags = tags
        set_debug(debug)

    def execute(self, input: str) -> Any:
        return self._chain.invoke(
            {"input": input, "instructions": self._parser.get_format_instructions()},
            config={"run_name": self._assistant_name, "tags": self._tags}
        )