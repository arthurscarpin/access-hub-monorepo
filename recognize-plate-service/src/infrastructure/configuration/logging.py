import json
import logging
import sys
from pathlib import Path
from datetime import datetime, timezone

from infrastructure.configuration.settings import get_settings


class NDJSONFormatter(logging.Formatter):

    def format(self, record: logging.LogRecord) -> str:
        log = {
            "timestamp": datetime.now(timezone.utc).isoformat(),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
        }

        if record.exc_info:
            log["exception"] = self.formatException(record.exc_info)

        return json.dumps(log, ensure_ascii=False)


class ConsoleFormatter(logging.Formatter):

    def format(self, record: logging.LogRecord) -> str:
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        return (
            f"{timestamp} | "
            f"{record.levelname} | "
            f"{record.name} | "
            f"{record.getMessage()}"
        )


def get_logger() -> logging.Logger:
    logger = logging.getLogger("recognize-plate-service")

    if logger.handlers:
        return logger

    settings = get_settings()
    env = settings.ENVIRONMENT.lower()

    log_level = logging.DEBUG if env != "prod" else logging.INFO
    logger.setLevel(log_level)

    logger.propagate = False

    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(ConsoleFormatter())
    logger.addHandler(console_handler)

    if env != "prod":
        root = Path(__file__).resolve().parents[3]
        log_dir = root / "logs"
        log_dir.mkdir(parents=True, exist_ok=True)

        log_file = log_dir / f"{datetime.now():%Y%m%d}.ndjson"

        file_handler = logging.FileHandler(
            filename=log_file,
            encoding="utf-8"
        )
        file_handler.setFormatter(NDJSONFormatter())

        logger.addHandler(file_handler)

    return logger


logger = get_logger()