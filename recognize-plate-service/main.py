import os

from pathlib import Path
from dotenv import load_dotenv

from consumer import OCRConsumer


ROOT = Path(__file__).resolve().parents[1]
load_dotenv(ROOT / ".env.idea" if (ROOT / ".env.idea").exists() else ROOT / ".env")


def main() -> None:
    consumer = OCRConsumer(
        host=os.environ["RABBITMQ_HOST"],
        port=int(os.environ["RABBITMQ_PORT"]),
        user=os.environ["RABBITMQ_USERNAME"],
        password=os.environ["RABBITMQ_PASSWORD"],
        queue=os.environ["RABBITMQ_QUEUE"],
    )
    consumer.start()

if __name__ == "__main__":
    main()