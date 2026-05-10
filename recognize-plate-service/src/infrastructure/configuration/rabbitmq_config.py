from pika import BlockingConnection, ConnectionParameters, PlainCredentials

class RabbitMQConfig:

    @staticmethod
    def create(host: str, port: int, user: str, password: str) -> BlockingConnection:
        plain_credentials = PlainCredentials(user, password)
        connection_parameters = ConnectionParameters(host=host, port=port, credentials=plain_credentials)
        return BlockingConnection(connection_parameters)
