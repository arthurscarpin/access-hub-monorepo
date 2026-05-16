import json
import pytest
from unittest.mock import Mock, patch

from infrastructure.consumer.capture_consumer import CaptureConsumer


@pytest.fixture
def mock_pika():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    return connection, channel


@pytest.fixture
def consumer_params(mock_pika):
    connection, _ = mock_pika
    return {
        "connection": connection,
        "queue": "q",
        "use_case": Mock(),
        "gateway": Mock(),
        "exchange": "ex",
        "routing_key": "rk",
        "logger": Mock(),
        "storage": "/tmp",
        "max_retries": 3,
        "base_delay_seconds": 2,
    }


@pytest.fixture
def consumer(consumer_params):
    return CaptureConsumer(**consumer_params)


def test_init_configures_channel(consumer_params, mock_pika):
    _, channel = mock_pika
    CaptureConsumer(**consumer_params)
    channel.basic_qos.assert_called_once_with(prefetch_count=1)


def test_callback_success(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    method = Mock(delivery_tag=123)
    body = json.dumps({"filename": "file.jpg"}).encode()

    consumer._callback(channel, method, Mock(headers={}), body)

    channel.basic_ack.assert_called_once_with(delivery_tag=123)
    channel.basic_nack.assert_not_called()
    consumer_params["use_case"].execute.assert_called_once()


def test_callback_json_error_nacks_without_retry(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    method = Mock(delivery_tag=1)

    consumer._callback(channel, method, Mock(headers={}), b"invalid-json")

    channel.basic_nack.assert_called_once_with(delivery_tag=1, requeue=False)
    channel.basic_ack.assert_not_called()
    channel.basic_publish.assert_not_called()
    consumer_params["logger"].error.assert_called()


def test_callback_exception_retries_on_first_attempt(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    consumer_params["use_case"].execute.side_effect = Exception("transient error")

    method = Mock(delivery_tag=1)
    body = json.dumps({"filename": "file.jpg"}).encode()
    properties = Mock(headers={})

    with patch("time.sleep") as mock_sleep:
        consumer._callback(channel, method, properties, body)

    channel.basic_ack.assert_called_once_with(delivery_tag=method.delivery_tag)
    channel.basic_nack.assert_not_called()

    channel.basic_publish.assert_called_once()
    published_properties = channel.basic_publish.call_args.kwargs["properties"]
    assert published_properties.headers["x-retry-count"] == 1

    mock_sleep.assert_called_once_with(2 ** 0)


def test_callback_exception_retries_on_second_attempt(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    consumer_params["use_case"].execute.side_effect = Exception("transient error")

    method = Mock(delivery_tag=2)
    body = json.dumps({"filename": "file.jpg"}).encode()
    properties = Mock(headers={"x-retry-count": 1})

    with patch("time.sleep") as mock_sleep:
        consumer._callback(channel, method, properties, body)

    channel.basic_ack.assert_called_once_with(delivery_tag=method.delivery_tag)
    channel.basic_nack.assert_not_called()

    published_properties = channel.basic_publish.call_args.kwargs["properties"]
    assert published_properties.headers["x-retry-count"] == 2

    mock_sleep.assert_called_once_with(2 ** 1)


def test_callback_exception_sends_to_dlq_after_max_retries(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    consumer_params["use_case"].execute.side_effect = Exception("persistent error")

    method = Mock(delivery_tag=99)
    body = json.dumps({"filename": "file.jpg"}).encode()
    properties = Mock(headers={"x-retry-count": 3})

    consumer._callback(channel, method, properties, body)

    channel.basic_nack.assert_called_once_with(delivery_tag=99, requeue=False)
    channel.basic_ack.assert_not_called()
    channel.basic_publish.assert_not_called()
    consumer_params["logger"].error.assert_called()


def test_get_retry_count_returns_zero_when_no_header(consumer):
    assert consumer._get_retry_count(Mock(headers=None)) == 0


def test_get_retry_count_returns_value_from_header(consumer):
    assert consumer._get_retry_count(Mock(headers={"x-retry-count": 2})) == 2


def test_start_registers_consume(consumer, mock_pika):
    _, channel = mock_pika
    consumer.start()
    channel.basic_consume.assert_called_once()
    channel.start_consuming.assert_called_once()


def test_start_keyboard_interrupt(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    channel.start_consuming.side_effect = KeyboardInterrupt()

    with patch.object(CaptureConsumer, '_stop') as mock_stop:
        consumer.start()
        mock_stop.assert_called_once()
        consumer_params["logger"].warning.assert_called_with(
            "[ctrl] + [c] pressed ending Capture Consumer"
        )


def test_start_finally_block(consumer, consumer_params, mock_pika):
    _, channel = mock_pika
    channel.start_consuming.return_value = None
    consumer.start()
    consumer_params["logger"].info.assert_any_call("Consumer Capture finished")


def test_stop_closes_open_resources(consumer, mock_pika):
    connection, channel = mock_pika
    channel.is_open = True
    connection.is_open = True

    consumer._stop()

    channel.stop_consuming.assert_called_once()
    connection.close.assert_called_once()


def test_stop_already_closed_resources(consumer, mock_pika):
    connection, channel = mock_pika
    channel.is_open = False
    connection.is_open = False

    consumer._stop()

    channel.stop_consuming.assert_not_called()
    connection.close.assert_not_called()


def test_stop_exception_handling(consumer, consumer_params, mock_pika):
    connection, channel = mock_pika
    channel.is_open = True
    channel.stop_consuming.side_effect = Exception("Network Error")

    consumer._stop()

    consumer_params["logger"].error.assert_called_once()