import json
from unittest.mock import Mock, patch

from infrastructure.consumer.capture_consumer  import CaptureConsumer


def test_callback_success():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    use_case = Mock()
    gateway = Mock()
    logger = Mock()
    use_case.execute.return_value = {"status": "ok"}
    consumer = CaptureConsumer(
        connection=connection,
        queue="q",
        use_case=use_case,
        gateway=gateway,
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )
    ch = channel
    method = Mock()
    method.delivery_tag = 123
    body = json.dumps({"filename": "file.jpg"}).encode()
    consumer._callback(ch, method, None, body)
    ch.basic_ack.assert_called_once_with(delivery_tag=123)
    use_case.execute.assert_called_once()

def test_callback_invalid_json():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    use_case = Mock()
    logger = Mock()
    consumer = CaptureConsumer(
        connection=connection,
        queue="q",
        use_case=use_case,
        gateway=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )
    ch = channel
    method = Mock()
    method.delivery_tag = 1
    body = b"invalid-json"
    consumer._callback(ch, method, None, body)
    ch.basic_nack.assert_called_once_with(delivery_tag=1, requeue=False)
    logger.error.assert_called()

def test_callback_usecase_exception():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    use_case = Mock()
    use_case.execute.side_effect = Exception("boom")
    logger = Mock()
    consumer = CaptureConsumer(
        connection=connection,
        queue="q",
        use_case=use_case,
        gateway=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )
    ch = channel
    method = Mock()
    method.delivery_tag = 10
    body = json.dumps({"filename": "file.jpg"}).encode()
    consumer._callback(ch, method, None, body)
    ch.basic_nack.assert_called_once()
    logger.error.assert_called()

def test_start_registers_consume():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    consumer = CaptureConsumer(
        connection=connection,
        queue="q",
        use_case=Mock(),
        gateway=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=Mock(),
        storage="/tmp"
    )
    channel.basic_consume = Mock()
    channel.start_consuming = Mock()
    consumer.start()
    channel.basic_consume.assert_called_once()
    channel.start_consuming.assert_called_once()

def test_start_keyboard_interrupt():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    channel.start_consuming.side_effect = KeyboardInterrupt()
    logger = Mock()
    consumer = CaptureConsumer(
        connection=connection, queue="q", use_case=Mock(), 
        gateway=Mock(), exchange="ex", routing_key="rk",
        logger=logger, storage="/tmp"
    )
    with patch.object(CaptureConsumer, '_stop') as mock_stop:
        consumer.start()
        mock_stop.assert_called_once()
        logger.warning.assert_called_with("[ctrl] + [c] pressed ending Capture Consumer")

def test_stop_closes_open_resources():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    channel.is_open = True
    connection.is_open = True
    consumer = CaptureConsumer(
        connection=connection, queue="q", use_case=Mock(), 
        gateway=Mock(), exchange="ex", routing_key="rk",
        logger=Mock(), storage="/tmp"
    )
    consumer._stop()
    channel.stop_consuming.assert_called_once()
    connection.close.assert_called_once()

def test_stop_already_closed_resources():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    channel.is_open = False
    connection.is_open = False
    consumer = CaptureConsumer(
        connection=connection, queue="q", use_case=Mock(), 
        gateway=Mock(), exchange="ex", routing_key="rk",
        logger=Mock(), storage="/tmp"
    )
    consumer._stop()
    channel.stop_consuming.assert_not_called()
    connection.close.assert_not_called()

def test_stop_exception_handling():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    channel.is_open = True
    channel.stop_consuming.side_effect = Exception("Network Error")
    logger = Mock()
    consumer = CaptureConsumer(
        connection=connection, queue="q", use_case=Mock(), 
        gateway=Mock(), exchange="ex", routing_key="rk",
        logger=logger, storage="/tmp"
    )
    consumer._stop()
    logger.error.assert_called_once()
    assert "Error Exception: Network Error" in logger.error.call_args[0][0]