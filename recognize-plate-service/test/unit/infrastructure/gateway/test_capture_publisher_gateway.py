
from unittest.mock import Mock, patch

from infrastructure.gateway.capture_publisher_gateway import CapturePublisherGateway


def test_message_publisher():
    gateway = CapturePublisherGateway()

    connection = Mock()

    with patch("infrastructure.gateway.capture_publisher_gateway.OCRStatusProducer") as ProducerMock:
        producer_instance = Mock()
        ProducerMock.return_value = producer_instance

        gateway.message_publisher(
            message={"a": "1"},
            connection=connection,
            exchange="ex",
            routing_key="rk",
        )

        ProducerMock.assert_called_once_with(
            connection=connection,
            exchange="ex",
        )

        producer_instance.publish.assert_called_once_with(
            routing_key="rk",
            payload={"a": "1"},
        )

def test_storage_build_path():
    gateway = CapturePublisherGateway()

    with patch("infrastructure.gateway.capture_publisher_gateway.Storage") as StorageMock:
        instance = Mock()
        instance.build.return_value = "/tmp/file.jpg"
        StorageMock.return_value = instance

        result = gateway.storage_build_path("file.jpg", "/tmp")

        assert result == "/tmp/file.jpg"
        instance.build.assert_called_once_with(filename="file.jpg")

def test_image_preprocessor():
    gateway = CapturePublisherGateway()

    with patch("infrastructure.gateway.capture_publisher_gateway.OpenCVPreProcessor") as MockProc:
        instance = Mock()
        instance.execute.return_value = "img"
        MockProc.return_value = instance

        result = gateway.image_preprocessor("/tmp/file.jpg")

        assert result == "img"
        instance.execute.assert_called_once_with(path="/tmp/file.jpg")

def test_ocr_processor():
    gateway = CapturePublisherGateway()

    with patch("infrastructure.gateway.capture_publisher_gateway.EasyOCRProcessor") as MockOCR:
        instance = Mock()
        instance.execute.return_value = [{"text": "ABC"}]
        MockOCR.return_value = instance

        result = gateway.ocr_processor("img")

        assert result == [{"text": "ABC"}]
        instance.execute.assert_called_once_with(image="img")