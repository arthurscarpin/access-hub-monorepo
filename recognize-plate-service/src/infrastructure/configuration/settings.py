from pathlib import Path
from pydantic_settings import BaseSettings, SettingsConfigDict

ROOT_DIR = Path(__file__).resolve().parents[4]
env_idea = ROOT_DIR / ".env.idea"
env = ROOT_DIR / ".env"


class Settings(BaseSettings):
    RABBITMQ_HOST: str
    RABBITMQ_PORT: int
    RABBITMQ_USERNAME: str
    RABBITMQ_PASSWORD: str
    RABBITMQ_QUEUE: str
    RABBITMQ_OCR_STATUS_EXCHANGE: str
    RABBITMQ_OCR_STATUS_ROUTING_KEY: str
    STORAGE_PATH: str

    model_config = SettingsConfigDict(
        env_file=env_idea if env_idea.exists() else env, 
        extra="ignore"
    )

settings = Settings() # type: ignore