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
    def consumer_params(self, mock_pika: tuple[MagicMock, MagicMock]) -> dict:
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
            "key": "secret",
            "max_retries": 3,
            "base_delay_seconds": 2,
        }

    @pytest.fixture
    def consumer(self, consumer_params, mock_pika):
        return PlateConsumer(**consumer_params)

    def test_init_configures_channel(self, consumer_params, mock_pika):
        _, channel = mock_pika
        PlateConsumer(**consumer_params)
        channel.basic_qos.assert_called_once_with(prefetch_count=1)

    def test_callback_success(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        method = MagicMock(delivery_tag=1)
        body = json.dumps({"id": "test-123"}).encode("utf-8")

        consumer._callback(channel, method, MagicMock(headers={}), body)

        consumer_params["use_case"].execute.assert_called_once()
        channel.basic_ack.assert_called_once_with(delivery_tag=1)
        channel.basic_nack.assert_not_called()

    def test_callback_json_error_nacks_without_retry(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        method = MagicMock(delivery_tag=1)

        consumer._callback(channel, method, MagicMock(headers={}), b"invalid-json")

        channel.basic_nack.assert_called_once_with(delivery_tag=1, requeue=False)
        channel.basic_ack.assert_not_called()
        channel.basic_publish.assert_not_called()
        consumer_params["logger"].error.assert_called()

    def test_callback_exception_retries_on_first_attempt(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        consumer_params["use_case"].execute.side_effect = Exception("transient error")

        method = MagicMock(delivery_tag=1)
        body = json.dumps({"id": "1"}).encode("utf-8")
        properties = MagicMock(headers={})

        with patch("time.sleep") as mock_sleep:
            consumer._callback(channel, method, properties, body)

        channel.basic_ack.assert_called_once_with(delivery_tag=method.delivery_tag)
        channel.basic_nack.assert_not_called()

        channel.basic_publish.assert_called_once()
        published_properties = channel.basic_publish.call_args.kwargs["properties"]
        assert published_properties.headers["x-retry-count"] == 1

        mock_sleep.assert_called_once_with(2 ** 0)

    def test_callback_exception_retries_on_second_attempt(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        consumer_params["use_case"].execute.side_effect = Exception("transient error")

        method = MagicMock(delivery_tag=2)
        body = json.dumps({"id": "1"}).encode("utf-8")
        properties = MagicMock(headers={"x-retry-count": 1})

        with patch("time.sleep") as mock_sleep:
            consumer._callback(channel, method, properties, body)

        channel.basic_ack.assert_called_once_with(delivery_tag=method.delivery_tag)
        channel.basic_nack.assert_not_called()

        published_properties = channel.basic_publish.call_args.kwargs["properties"]
        assert published_properties.headers["x-retry-count"] == 2

        mock_sleep.assert_called_once_with(2 ** 1)

    def test_callback_exception_sends_to_dlq_after_max_retries(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        consumer_params["use_case"].execute.side_effect = Exception("persistent error")

        method = MagicMock(delivery_tag=99)
        body = json.dumps({"id": "1"}).encode("utf-8")
        properties = MagicMock(headers={"x-retry-count": 3})

        consumer._callback(channel, method, properties, body)

        channel.basic_nack.assert_called_once_with(delivery_tag=99, requeue=False)
        channel.basic_ack.assert_not_called()
        channel.basic_publish.assert_not_called()
        consumer_params["logger"].error.assert_called()

    def test_get_retry_count_returns_zero_when_no_header(self, consumer):
        assert consumer._get_retry_count(MagicMock(headers=None)) == 0

    def test_get_retry_count_returns_value_from_header(self, consumer):
        assert consumer._get_retry_count(MagicMock(headers={"x-retry-count": 2})) == 2

    def test_start_listening(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        channel.start_consuming.side_effect = KeyboardInterrupt()

        with patch.object(PlateConsumer, '_stop') as mock_stop:
            consumer.start()
            channel.basic_consume.assert_called_once_with(
                queue="plate_queue",
                on_message_callback=consumer._callback
            )
            mock_stop.assert_called_once()

    def test_start_finally_block(self, consumer, consumer_params, mock_pika):
        _, channel = mock_pika
        channel.start_consuming.return_value = None
        consumer.start()
        consumer_params["logger"].info.assert_any_call("Consumer Capture finished")

    def test_stop_closes_resources(self, consumer, mock_pika):
        connection, channel = mock_pika
        channel.is_open = True
        connection.is_open = True

        consumer._stop()

        channel.stop_consuming.assert_called_once()
        connection.close.assert_called_once()

    def test_stop_channel_and_connection_already_closed(self, consumer, mock_pika):
        connection, channel = mock_pika
        channel.is_open = False
        connection.is_open = False

        consumer._stop()

        channel.stop_consuming.assert_not_called()
        connection.close.assert_not_called()

    def test_stop_exception_handling(self, consumer, consumer_params, mock_pika):
        connection, channel = mock_pika
        channel.is_open = True
        channel.stop_consuming.side_effect = Exception("Erro ao fechar")

        consumer._stop()

        consumer_params["logger"].error.assert_called_once()