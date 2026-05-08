import json
import pika
from pathlib import Path

from pika.adapters.blocking_connection import BlockingChannel, BlockingConnection
from pika.spec import Basic, BasicProperties

from pipelines import PlatePipeline
from configuration import StorageConfig, logger
from service import OCRService, PlateService, PreProcessorImageService


class OCRConsumer:
    _connection: BlockingConnection
    _channel: BlockingChannel
    _queue: str
    _pipeline: PlatePipeline

    def __init__(
        self,
        host: str,
        port: int,
        user: str,
        password: str,
        queue: str,
    ):
        self._queue = queue

        logger.info("Initializing OCRConsumer...")

        self._connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=host,
                port=port,
                credentials=pika.PlainCredentials(user, password),
            )
        )

        self._channel = self._connection.channel()
        self._channel.basic_qos(prefetch_count=1)

        storage = StorageConfig(
            root=Path(__file__).resolve().parents[2],
            folder="storage",
        )

        self._pipeline = PlatePipeline(
            storage=storage,
            image_service=PreProcessorImageService(),
            ocr_service=OCRService(),
            plate_service=PlateService(),
        )

        logger.info(f"OCRConsumer ready | queue={queue}")

    def callback(
        self,
        ch: BlockingChannel,
        method: Basic.Deliver,
        properties: BasicProperties,
        body: bytes,
    ) -> None:
        delivery_tag = method.delivery_tag

        try:
            message = json.loads(body.decode("utf-8"))
            logger.info(f"Message received | body={message}")

            filename = message.get("filename")

            if not filename:
                logger.warning("Invalid message: missing filename")
                ch.basic_nack(delivery_tag=delivery_tag, requeue=False)
                return

            logger.info(f"Processing file | filename={filename}")

            result = self._pipeline.run(filename)

            logger.info(f"Pipeline completed | filename={filename}")

            print(result.model_dump_json(indent=2, ensure_ascii=False))

            ch.basic_ack(delivery_tag=delivery_tag)

        except json.JSONDecodeError:
            logger.error("Invalid JSON message received", exc_info=True)
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)

        except Exception as e:
            logger.error(
                f"Error processing message | error={str(e)}",
                exc_info=True
            )
            ch.basic_nack(delivery_tag=delivery_tag, requeue=False)

    def start(self) -> None:
        try:
            self._channel.basic_consume(
                queue=self._queue,
                on_message_callback=self.callback,
            )

            logger.info(f"Consumer started | queue={self._queue}")
            self._channel.start_consuming()

        except KeyboardInterrupt:
            logger.warning("Consumer stopped manually")
            self._connection.close()

        except Exception as e:
            logger.error(f"Consumer crashed | error={str(e)}", exc_info=True)
            self._connection.close()