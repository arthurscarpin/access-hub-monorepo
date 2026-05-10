from pathlib import Path
from pydantic_settings import BaseSettings, SettingsConfigDict

ROOT_DIR = Path(__file__).resolve().parents[4]

class Settings(BaseSettings):
    RABBITMQ_HOST: str
    RABBITMQ_PORT: int
    RABBITMQ_USERNAME: str
    RABBITMQ_PASSWORD: str
    RABBITMQ_QUEUE: str
    RABBITMQ_OCR_STATUS_EXCHANGE: str
    RABBITMQ_OCR_STATUS_ROUTING_KEY: str

    model_config = SettingsConfigDict(
        env_file=ROOT_DIR / ".env.idea", 
        extra="ignore"
    )

settings = Settings() # type: ignore