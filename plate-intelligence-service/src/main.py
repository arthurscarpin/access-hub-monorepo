from core import ValidatePlateUseCaseUseCaseImpl
from infrastructure import RabbitMQConfig, get_settings, PlateAnlysisGateway, logger, PlateConsumer


def main():
    settings = get_settings()
    gateway = PlateAnlysisGateway()
    use_case = ValidatePlateUseCaseUseCaseImpl()

    connection = RabbitMQConfig.create(
        host=settings.RABBITMQ_HOST, 
        port=settings.RABBITMQ_PORT, 
        user=settings.RABBITMQ_USERNAME, 
        password=settings.RABBITMQ_PASSWORD
    )

    consumer = PlateConsumer(
        connection=connection,
        queue=settings.RABBITMQ_AI_VALIDATION_QUEUE,
        use_case=use_case,
        gateway=gateway,
        exchange=settings.RABBITMQ_EXCHANGE, 
        routing_key=settings.RABBITMQ_AI_RESULT_ROUTING_KEY,
        logger=logger,
        model=settings.LLM_MODEL,
        temperature=settings.TEMPERATURE, 
        max_tokens=settings.MAX_TOKENS, 
        langchain_debug=settings.LANGCHAIN_DEBUG,
        key=settings.OPENAI_API_KEY,
        max_retries=settings.RABBITMQ_MAX_RETRIES,
        base_delay_seconds=settings.RABBITMQ_BASE_DELAY_SECONDS
    )
    consumer.start()

if __name__ == "__main__":
    main()