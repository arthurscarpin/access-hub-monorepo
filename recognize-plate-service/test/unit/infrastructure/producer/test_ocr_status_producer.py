import json
from typing import Any
import pytest
from unittest.mock import Mock

from infrastructure.producer.ocr_status_producer import OCRStatusProducer

def test_publish_success():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel
    
    producer = OCRStatusProducer(connection, exchange="ex")
    payload = {"status": "ok"}
    producer.publish("rk", payload)

    channel.basic_publish.assert_called_once()
    args, kwargs = channel.basic_publish.call_args
    assert kwargs["exchange"] == "ex"
    assert kwargs["routing_key"] == "rk"
    assert json.loads(kwargs["body"].decode()) == payload

def test_publish_properties():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel

    producer = OCRStatusProducer(connection, exchange="ex")
    producer.publish("rk", {"a": 1})
    props = channel.basic_publish.call_args.kwargs["properties"]

    assert props.content_type == "application/json"
    assert props.delivery_mode == 2

def test_publish_exception():
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel

    channel.basic_publish.side_effect = Exception("boom")
    producer = OCRStatusProducer(connection, exchange="ex")

    with pytest.raises(Exception):
        producer.publish("rk", {"a": 1})

def test_publish_logs(monkeypatch: Any):
    connection = Mock()
    channel = Mock()
    connection.channel.return_value = channel

    import infrastructure.producer.ocr_status_producer as mod
    log_mock = Mock()
    monkeypatch.setattr(mod, "logger", log_mock)

    producer = OCRStatusProducer(connection, exchange="ex")
    producer.publish("rk", {"a": 1})
    
    log_mock.info.assert_called()