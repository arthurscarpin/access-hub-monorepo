import pytest
import json
from unittest.mock import MagicMock, patch

from infrastructure.consumer.plate_consumer import PlateConsumer

class TestPlateConsumer:

    @pytest.fixture
    def mock_pika(self):
        connection = MagicMock()
        channel = MagicMock()
        connection.channel.return_value = channel
        return connection, channel

    @pytest.fixture
    def consumer_params(self, mock_pika: tuple[MagicMock, MagicMock]) -> dict[str, str]:
        conn, _ = mock_pika
        return {
            "connection": conn,
            "queue": "plate_queue",
            "use_case": MagicMock(),
            "gateway": MagicMock(),
            "exchange": "test_exchange",
            "routing_key": "test_key",
            "logger": MagicMock(),
            "model": "gpt-4",
            "temperature": 0.0,
            "max_tokens": 100,
            "langchain_debug": False,
            "key": "secret"
        }

    def test_init_configures_channel(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        
        _, channel = mock_pika
        
        PlateConsumer(**consumer_params)
        
        channel.basic_qos.assert_called_once_with(prefetch_count=1)

    def test_callback_success(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        method = MagicMock(delivery_tag=1)
        body = json.dumps({"id": "test-123"}).encode("utf-8")
        
        consumer._callback(channel, method, MagicMock(), body)

        consumer_params["use_case"].execute.assert_called_once()
        channel.basic_ack.assert_called_once_with(delivery_tag=1)

    def test_callback_json_error_triggers_nack(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        method = MagicMock(delivery_tag=1)
        body = b"invalid-json"
        
        consumer._callback(channel, method, MagicMock(), body)

        channel.basic_nack.assert_called_once_with(delivery_tag=1, requeue=False)
        consumer_params["logger"].error.assert_called()

    def test_callback_use_case_exception_triggers_nack(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        consumer_params["use_case"].execute.side_effect = Exception("Logic Error")
        
        method = MagicMock(delivery_tag=99)
        body = json.dumps({"id": "1"}).encode("utf-8")
        
        consumer._callback(channel, method, MagicMock(), body)

        channel.basic_nack.assert_called_once_with(delivery_tag=99, requeue=False)

    def test_start_listening(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        channel.start_consuming.side_effect = KeyboardInterrupt()

        with patch.object(PlateConsumer, '_stop') as mock_stop:
            consumer.start()
            
            channel.basic_consume.assert_called_once_with(
                queue="plate_queue", 
                on_message_callback=consumer._callback
            )
            mock_stop.assert_called_once()

    def test_stop_closes_resources(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        connection, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        channel.is_open = True
        connection.is_open = True
        
        consumer._stop()
        
        channel.stop_consuming.assert_called_once()
        connection.close.assert_called_once()

    def test_stop_channel_and_connection_already_closed(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        connection, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        channel.is_open = False
        connection.is_open = False
        
        consumer._stop()
        
        channel.stop_consuming.assert_not_called()
        connection.close.assert_not_called()

    def test_stop_exception_handling(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        connection, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        channel.is_open = True
        channel.stop_consuming.side_effect = Exception("Erro ao fechar")
        consumer._stop()
        
        consumer_params["logger"].error.assert_called_once()

    def test_start_finally_block(self, consumer_params: dict[str, str], mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        consumer = PlateConsumer(**consumer_params)
        
        channel.start_consuming.return_value = None
        consumer.start()
        
        consumer_params["logger"].info.assert_any_call("Consumer Capture finished")