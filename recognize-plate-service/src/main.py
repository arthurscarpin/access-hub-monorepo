from core import CaptureUseCaseImpl
from infrastructure.configuration.settings import get_settings
from infrastructure import RabbitMQConfig, CaptureConsumer, CapturePublisherGateway, logger


def main():
    settings = get_settings()
    capture_usecase = CaptureUseCaseImpl()
    capture_gateway = CapturePublisherGateway()

    connection = RabbitMQConfig.create(
        host=settings.RABBITMQ_HOST, 
        port=settings.RABBITMQ_PORT, 
        user=settings.RABBITMQ_USERNAME, 
        password=settings.RABBITMQ_PASSWORD
    )
    consumer = CaptureConsumer(
        connection=connection, 
        queue=settings.RABBITMQ_QUEUE, 
        use_case=capture_usecase, 
        gateway=capture_gateway,
        exchange=settings.RABBITMQ_OCR_STATUS_EXCHANGE, 
        routing_key=settings.RABBITMQ_OCR_STATUS_ROUTING_KEY,
        logger=logger,
        storage=settings.STORAGE_PATH
    )
    consumer.start()

if __name__ == "__main__":
    main()