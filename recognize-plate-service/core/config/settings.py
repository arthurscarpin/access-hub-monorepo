import os
from pathlib import Path
from dotenv import load_dotenv


class Settings:

    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(Settings, cls).__new__(cls)
            cls._instance._load()
        return cls._instance

    def _load(self):
        root = Path(__file__).resolve().parents[3]
        env_file = root / ".env.idea" if (root / ".env.idea").exists() else root / ".env"
        load_dotenv(env_file)

        # Environments Variables
        self.RABBITMQ_HOST = self._get("RABBITMQ_HOST")
        self.RABBITMQ_PORT = int(self._get("RABBITMQ_PORT"))
        self.RABBITMQ_USERNAME = self._get("RABBITMQ_USERNAME")
        self.RABBITMQ_PASSWORD = self._get("RABBITMQ_PASSWORD")
        self.RABBITMQ_QUEUE = self._get("RABBITMQ_QUEUE")
        
    def _get(self, key: str) -> str:
        value = os.getenv(key)
        if not value:
            raise EnvironmentError(f"Missing environment variable: {key}")
        return value
    
settings = Settings()