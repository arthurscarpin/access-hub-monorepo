from infra import RoutingKeyEnum

class RabbitMQEventPublisher:

    def __init__(self, producer):
        self._producer = producer

    def started(self, payload: dict):
        self._producer.publish(RoutingKeyEnum.STARTED.value, payload)

    def processing(self, payload: dict):
        self._producer.publish(RoutingKeyEnum.PROCESSING.value, payload)

    def completed(self, payload: dict):
        self._producer.publish(RoutingKeyEnum.COMPLETED.value, payload)

    def failed(self, payload: dict):
        self._producer.publish(RoutingKeyEnum.FAILED.value, payload)