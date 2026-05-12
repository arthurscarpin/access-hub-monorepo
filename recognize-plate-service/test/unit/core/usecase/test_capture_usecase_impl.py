from unittest.mock import Mock
from core.usecase.capture_usecase_impl import CaptureUseCaseImpl
from core.domain.status import ImageStatus, CaptureStatus

def test_execute_without_filename():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()

    result = usecase.execute(
        message={},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    assert result["image_status"] == ImageStatus.FAILED.value
    assert result["capture_status"] == CaptureStatus.PROCESSING.value
    assert result["message"] == "Filename is not found"
    assert result["ocr"] == []
    gateway.message_publisher.assert_called_once()
    logger.error.assert_called_once_with("Filename is not found")

def test_execute_success_flow():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()

    mock_ocr_response = [
        {"text": "HAT", "confidence": 0.48},
        {"text": "R102a19?", "confidence": 0.29}
    ]

    gateway.storage_build_path.return_value = "/tmp/car1.webp"
    gateway.image_preprocessor.return_value = "img_processed"
    gateway.ocr_processor.return_value = mock_ocr_response

    message = {
        "captureId": "6a03414bc2d067e22df02148",
        "imageId": "0e347d43-8f72-4c2e-ab72-c1416a71266d",
        "filename": "car1.webp"
    }

    result = usecase.execute(
        message=message,
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    assert result["image_status"] == ImageStatus.COMPLETED.value
    assert result["capture_status"] == CaptureStatus.PROCESSING.value
    assert result["ocr"] == mock_ocr_response
    assert result["message"] == "Execution completed"
    assert gateway.message_publisher.call_count == 3

def test_execute_exception_flow():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()
    
    error_msg = "Storage failure"
    gateway.storage_build_path.side_effect = Exception(error_msg)

    result = usecase.execute(
        message={"filename": "car1.webp"},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    assert result["image_status"] == ImageStatus.FAILED.value
    assert result["capture_status"] == CaptureStatus.PROCESSING.value
    assert str(result["message"]) == error_msg
    assert result["ocr"] == []
    assert gateway.message_publisher.call_count >= 1
    logger.error.assert_called()

def test_message_publisher_called_with_started_status():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()
    
    gateway.storage_build_path.return_value = "/tmp/file.jpg"
    gateway.image_preprocessor.return_value = "img"
    gateway.ocr_processor.return_value = []

    usecase.execute(
        message={"filename": "file.jpg"},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    first_call_kwargs = gateway.message_publisher.call_args_list[0].kwargs
    assert first_call_kwargs["message"]["image_status"] == ImageStatus.STARTED.value
    assert first_call_kwargs["message"]["message"] == "Execution started"