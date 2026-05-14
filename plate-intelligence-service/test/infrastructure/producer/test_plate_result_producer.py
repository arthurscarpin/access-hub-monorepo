import pytest
import json
from typing import Any
from unittest.mock import MagicMock, patch
from infrastructure.producer.plate_result_producer import PlateResultProducer

class TestPlateResultProducer:

    @pytest.fixture
    def mock_pika(self):
        connection = MagicMock()
        channel = MagicMock()
        connection.channel.return_value = channel
        return connection, channel

    @pytest.fixture
    def producer(self, mock_pika: tuple[MagicMock, MagicMock]) -> PlateResultProducer:
        connection, _ = mock_pika
        return PlateResultProducer(connection=connection, exchange="test_exchange")

    def test_publish_success(self, producer: PlateResultProducer, mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        payload: dict[str, Any] = {"id": 1, "plate": "ABC1234"}
        routing_key = "success_key"

        producer.publish(routing_key, payload)

        expected_body = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        
        args, kwargs = channel.basic_publish.call_args
        assert kwargs["exchange"] == "test_exchange"
        assert kwargs["routing_key"] == routing_key
        assert kwargs["body"] == expected_body
        assert kwargs["properties"].content_type == "application/json"
        assert kwargs["properties"].delivery_mode == 2

    def test_publish_with_special_characters(self, producer: PlateResultProducer, mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        payload = {"reasoning": "Placa de São Paulo"}
        
        producer.publish("key", payload)
        
        sent_body = channel.basic_publish.call_args.kwargs["body"]
        decoded_body = json.loads(sent_body.decode("utf-8"))
        assert decoded_body["reasoning"] == "Placa de São Paulo"

    def test_publish_exception_logging_and_raise(self,producer: PlateResultProducer, mock_pika: tuple[MagicMock, MagicMock]):
        _, channel = mock_pika
        channel.basic_publish.side_effect = Exception("Connection Lost")
        
        with patch("infrastructure.producer.plate_result_producer.logger") as mock_logger:
            with pytest.raises(Exception, match="Connection Lost"):
                producer.publish("key", {"data": "test"})
            
            mock_logger.error.assert_called_once()

    def test_init_creates_channel(self, mock_pika: tuple[MagicMock, MagicMock]):
        connection, _ = mock_pika
        PlateResultProducer(connection=connection, exchange="ex")
        connection.channel.assert_called_once()