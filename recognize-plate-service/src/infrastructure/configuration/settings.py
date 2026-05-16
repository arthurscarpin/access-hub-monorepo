from pathlib import Path
from functools import lru_cache
from pydantic_settings import BaseSettings, SettingsConfigDict

ROOT_DIR = Path(__file__).resolve().parents[4]
env_idea = ROOT_DIR / ".env.idea"
env = ROOT_DIR / ".env"


class Settings(BaseSettings):
    ENVIRONMENT: str
    RABBITMQ_HOST: str
    RABBITMQ_PORT: int
    RABBITMQ_USERNAME: str
    RABBITMQ_PASSWORD: str
    RABBITMQ_EXCHANGE: str
    RABBITMQ_OCR_QUEUE: str
    RABBITMQ_OCR_STATUS_ROUTING_KEY: str
    RABBITMQ_AI_VALIDATION_ROUTING_KEY: str
    RABBITMQ_MAX_RETRIES: int
    RABBITMQ_BASE_DELAY_SECONDS: int
    STORAGE_PATH: str

    model_config = SettingsConfigDict(
        env_file=env_idea if env_idea.exists() else env,
        extra="ignore",
    )


@lru_cache
def get_settings() -> Settings:
    return Settings()