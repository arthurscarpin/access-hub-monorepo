from typing import TypeVar, Type, Self
from pathlib import Path

from pydantic import BaseModel
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

T = TypeVar("T", bound=BaseModel)

class ChatOpenAIConfig:

    def __init__(self, model: str, temperature: float, max_tokens: int, key: str, max_retries: int=3):
        self.llm = ChatOpenAI(
            model=model, 
            temperature=temperature,
            max_completion_tokens=max_tokens,
            max_retries=max_retries,
            api_key=key
        )
        self.prompt_template = None
        self.parser = None

    def _load_sytem_prompt(self, filename: str) -> str:
        path = Path(__file__).parent.parent.parent / "infrastructure" / "ai_assistants" / f"{filename}.md"
        with open(path, "r", encoding="utf-8") as f:
            return f.read()

    def set_prompt_template(self, system_prompt_filename: str, human_prompt: str) -> Self:
        messages: list[tuple[str, str]] = [
            ("system", self._load_sytem_prompt(system_prompt_filename)),
            ("human", human_prompt)
        ]
        self.prompt_template = ChatPromptTemplate.from_messages(messages) # type: ignore
        return self
    
    def set_parser(self, output_parser: Type[T]) -> Self:
        self.parser = JsonOutputParser(pydantic_object=output_parser)
        return self