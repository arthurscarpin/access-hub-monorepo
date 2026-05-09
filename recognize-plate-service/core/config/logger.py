import json
import logging
import sys
from pathlib import Path
from datetime import datetime


class NDJSONFormatter(logging.Formatter):

    def format(self, record: logging.LogRecord) -> str:
        log = {
            "timestamp": datetime.utcnow().isoformat(),
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


class Logger:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._configure()
        return cls._instance

    def _configure(self) -> None:
        self._logger = logging.getLogger("recognize-plate-service")

        if self._logger.handlers:
            return

        self._logger.setLevel(logging.INFO)

        root = Path(__file__).resolve().parents[2]

        log_dir = root / "logs"
        log_dir.mkdir(parents=True, exist_ok=True)

        log_file = log_dir / f"{datetime.now():%Y%m%d}.ndjson"

        console_formatter = ConsoleFormatter()
        ndjson_formatter = NDJSONFormatter()
        console_handler = logging.StreamHandler(sys.stdout)
        console_handler.setFormatter(console_formatter)

        file_handler = logging.FileHandler(
            filename=log_file,
            encoding="utf-8"
        )
        file_handler.setFormatter(ndjson_formatter)

        self._logger.addHandler(console_handler)
        self._logger.addHandler(file_handler)

    def get(self) -> logging.Logger:
        return self._logger


logger = Logger().get()