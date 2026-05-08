from consumer import OCRConsumer


def main() -> None:
    consumer = OCRConsumer(
        host="localhost",
        port=5672,
        user="rabbitmq",
        password="rabbitmq",
        queue="ocr-processing-queue",
    )
    consumer.start()

if __name__ == "__main__":
    main()