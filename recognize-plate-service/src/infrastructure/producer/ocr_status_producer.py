import json
from typing import Any

from pika import BasicProperties, BlockingConnection
from infrastructure.configuration.logging import logger


class OCRStatusProducer:

    def __init__(self, connection: BlockingConnection, exchange: str):
        self._connection = connection
        self._channel = connection.channel()
        self._exchange = exchange

    def publish(self, routing_key: str, payload: dict[str, Any]):
        try:
            message = json.dumps(payload, ensure_ascii=False).encode("utf-8")
            basic_properties = BasicProperties(content_type="application/json", delivery_mode=2)
            self._channel.basic_publish(exchange=self._exchange, routing_key=routing_key, body=message, properties=basic_properties)
            logger.info("The message has been sent")
            logger.info(f"{self._exchange=} | {routing_key=} | {message=}")
        except Exception as e:
            logger.error(f"Error {type(e).__name__}: {e}")
            raise