import json

from core import logger
from pika.adapters.blocking_connection import BlockingChannel, BlockingConnection


class RabbitMQConsumer:

    def __init__(self, connection, queue: str, handler):
        self._connection: BlockingConnection = connection
        self._channel: BlockingChannel = connection.channel()
        self._queue = queue
        self._handler = handler
        self._stopped = False

        self._channel.basic_qos(prefetch_count=1)

        logger.info(f"rabbitmq.consumer.initialized queue={queue}")

    def callback(self, ch, method, properties, body: bytes):
        delivery_tag = method.delivery_tag
        message_id = f"tag={delivery_tag}"

        try:
            message = json.loads(body.decode("utf-8"))
            logger.info(f"rabbitmq.message.received {message_id} payload={message}")

            self._process(message, message_id)

            ch.basic_ack(delivery_tag=delivery_tag)
            logger.info(f"rabbitmq.message.acked {message_id}")

        except json.JSONDecodeError as e:
            logger.error(
                f"rabbitmq.message.invalid_json {message_id} error={e}",
                exc_info=True,
            )
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)

        except Exception as e:
            logger.error(
                f"rabbitmq.message.failed {message_id} error={type(e).__name__}: {e}",
                exc_info=True,
            )
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)

    def _process(self, message, message_id: str):
        logger.info(f"rabbitmq.message.processing {message_id}")

        self._handler(message)

        logger.info(f"rabbitmq.message.processed {message_id}")

    def start(self):
        try:
            self._channel.basic_consume(
                queue=self._queue,
                on_message_callback=self.callback,
            )

            logger.info(f"rabbitmq.consumer.started queue={self._queue}")

            self._channel.start_consuming()

        except KeyboardInterrupt:
            logger.warning("rabbitmq.consumer.shutdown_signal ctrl_c")

            self.stop()

        finally:
            logger.info("rabbitmq.consumer.stopped")

    def stop(self):
        try:
            if self._channel.is_open:
                self._channel.stop_consuming()

            if self._connection.is_open:
                self._connection.close()

            logger.info("rabbitmq.connection.closed")

        except Exception as e:
            logger.error(
                f"rabbitmq.shutdown.error error={type(e).__name__}: {e}",
                exc_info=True,
            )