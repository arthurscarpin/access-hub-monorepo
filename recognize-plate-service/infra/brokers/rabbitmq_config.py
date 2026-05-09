from . import ExchangeEnum

class RabbitMQConfig:
    def __init__(self, channel):
        self._channel = channel

    def setup(self):
        exchanges = [
            ExchangeEnum.CAPTURE_EVENTS,
            ExchangeEnum.OCR_STATUS_EVENTS,
            ExchangeEnum.OCR_DLX,
        ]

        for exchange in exchanges:
            self._channel.exchange_declare(
                exchange=exchange.value,
                exchange_type="topic",
                durable=True
            )