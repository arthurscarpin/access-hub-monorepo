import pika


class RabbitMQConnector:

    @staticmethod
    def create(
        host: str, 
        port: int, 
        user: str, 
        password: str
    ) -> pika.BlockingConnection:
        return pika.BlockingConnection(
            pika.ConnectionParameters(
                host=host,
                port=port,
                credentials=pika.PlainCredentials(user, password),
            )
        )