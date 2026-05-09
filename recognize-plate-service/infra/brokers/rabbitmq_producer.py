import json
import pika
from core import logger


class RabbitMQProducer:

    def __init__(self, connection, exchange: str):
        self._connection = connection
        self._channel = connection.channel()
        self._exchange = exchange

        logger.info(f"rabbitmq.producer.initialized exchange={exchange}")

    def publish(self, routing_key: str, payload: dict) -> None:
        try:
            body = json.dumps(payload, ensure_ascii=False).encode("utf-8")

            logger.info(
                f"rabbitmq.publish.attempt exchange={self._exchange} routing_key={routing_key} payload_keys={list(payload.keys())}"
            )

            self._channel.basic_publish(
                exchange=self._exchange,
                routing_key=routing_key,
                body=body,
                properties=pika.BasicProperties(
                    content_type="application/json",
                    delivery_mode=2,
                ),
            )

            logger.info(
                f"rabbitmq.publish.success exchange={self._exchange} routing_key={routing_key}"
            )

        except Exception as e:
            logger.error(
                f"rabbitmq.publish.failed exchange={self._exchange} routing_key={routing_key} error={type(e).__name__}: {e}",
                exc_info=True,
            )
            raise