import uuid
from pathlib import Path
from time import perf_counter

from core import (
    logger,
    settings,
    Storage,
    PreProcessorImageService,
    OCRService,
    PlateService,
    RecognizeUseCase,
)
from infra import (
    RabbitMQConnector,
    RabbitMQProducer,
    ExchangeEnum,
    RabbitMQConsumer,
    RabbitMQEventPublisher,
)


def main() -> None:
    run_id = str(uuid.uuid4())
    start_time = perf_counter()

    logger.info(f"app.starting run_id={run_id} service=ocr-pipeline")

    try:
        logger.info(
            f"rabbitmq.connecting run_id={run_id} host={settings.RABBITMQ_HOST} port={settings.RABBITMQ_PORT} queue={settings.RABBITMQ_QUEUE}"
        )

        connection = RabbitMQConnector.create(
            host=settings.RABBITMQ_HOST,
            port=settings.RABBITMQ_PORT,
            user=settings.RABBITMQ_USERNAME,
            password=settings.RABBITMQ_PASSWORD,
        )

        logger.info(f"rabbitmq.connected run_id={run_id}")

        producer = RabbitMQProducer(
            connection=connection,
            exchange=ExchangeEnum.OCR_STATUS_EVENTS.value,
        )

        event_publisher = RabbitMQEventPublisher(producer)

        storage = Storage(
            root=Path(__file__).resolve().parents[1],
            folder="storage",
        )

        logger.info(f"storage.ready run_id={run_id} root={Path(__file__).resolve().parents[1]}")

        image_service = PreProcessorImageService()
        ocr_service = OCRService()
        plate_service = PlateService()

        logger.info(f"services.ready run_id={run_id} image=1 ocr=1 plate=1")

        use_case = RecognizeUseCase(
            storage=storage,
            image_service=image_service,
            ocr_service=ocr_service,
            plate_service=plate_service,
            event_publisher=event_publisher,
        )

        consumer = RabbitMQConsumer(
            connection=connection,
            queue=settings.RABBITMQ_QUEUE,
            handler=use_case.run,
        )

        logger.info(f"consumer.ready run_id={run_id} queue={settings.RABBITMQ_QUEUE}")

        logger.info(
            f"app.started run_id={run_id} startup_ms={int((perf_counter() - start_time) * 1000)}"
        )

        consumer.start()

    except Exception as exc:
        logger.error(
            f"app.failed run_id={run_id} error={type(exc).__name__}: {exc}",
            exc_info=True,
        )
        raise


if __name__ == "__main__":
    main()