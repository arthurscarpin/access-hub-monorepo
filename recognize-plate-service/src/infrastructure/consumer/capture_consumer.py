import json
import logging
from typing import Any

from core import CaptureUseCase, CaptureGateway

from pika import BlockingConnection
from pika.spec import Basic, BasicProperties
from pika.adapters.blocking_connection import BlockingChannel


class CaptureConsumer:

    def __init__(
            self, 
            connection: BlockingConnection, 
            queue: str, 
            use_case: CaptureUseCase, 
            gateway: CaptureGateway,
            exchange: str, 
            routing_key: str,
            logger: logging.Logger,
            storage: str):
        self._connection = connection
        self._channel: Any = connection.channel()
        self._queue = queue
        self._channel.basic_qos(prefetch_count=1)
        self._capture_usecase = use_case
        self._gateway = gateway
        self._exchange = exchange
        self._routing_key = routing_key
        self._logger = logger
        self._storage = storage

    def start(self):
        try:
            self._logger.info(f"Listening to queue [{self._queue}]...")
            self._channel.basic_consume(queue=self._queue, on_message_callback=self._callback)
            self._channel.start_consuming()
        except KeyboardInterrupt:
            self._stop()
            self._logger.warning("[ctrl] + [c] pressed ending Capture Consumer")
        finally:
            self._logger.info("Consumer Capture finished")
    
    def _callback(self, ch: BlockingChannel, method: Basic.Deliver, properties: BasicProperties, body: bytes):
        delivery_tag = method.delivery_tag
        message_id = f"tag={delivery_tag}"
        try:
            message = json.loads(body.decode("utf-8"))
            self._logger.info(f"Message received: {message_id=} {message=}")
            
            message_sended = self._capture_usecase.execute(
                message=message, 
                gateway=self._gateway, 
                connection=self._connection, 
                exchange=self._exchange, 
                routing_key=self._routing_key,
                logger=self._logger,
                storage=self._storage
            )

            ch.basic_ack(delivery_tag=delivery_tag)
            self._logger.info(f"Message sended: {message_id=} {message_sended=}")
        except json.JSONDecodeError as e:
            self._logger.error(f"Error {type(e).__name__}: {e}")
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)
        except Exception as e:
            self._logger.error(f"Error {type(e).__name__}: {e}")
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)

    def _stop(self):
        try:
            if self._channel.is_open:
                self._channel.stop_consuming()
            if self._connection.is_open:
                self._connection.close()
            self._logger.info("Connection closed with Capture Consumer")
        except Exception as e:
            self._logger.error(f"Error {type(e).__name__}: {e}")
    