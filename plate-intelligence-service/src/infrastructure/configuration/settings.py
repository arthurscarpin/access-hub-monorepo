from dotenv import load_dotenv
# from pathlib import Path
from functools import lru_cache
from pydantic_settings import BaseSettings, SettingsConfigDict

# ROOT_DIR = Path(__file__).resolve().parents[4]
# env_idea = ROOT_DIR / ".env.idea"
# env = ROOT_DIR / ".env"

load_dotenv()

class Settings(BaseSettings):
    OPENAI_API_KEY: str
    LANGCHAIN_DEBUG: bool
    MODEL: str
    TEMPERATURE: float
    MAX_TOKENS: int

    model_config = SettingsConfigDict(
        # env_file=env_idea if env_idea.exists() else env,
        env_file=".env",
        extra="ignore",
    )


@lru_cache
def get_settings() -> Settings:
    return Settings()