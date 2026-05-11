from unittest.mock import Mock

from core.usecase.capture_usecase_impl import CaptureUseCaseImpl
from core.domain.capture_status import CaptureStatus

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

    assert result["status"] == CaptureStatus.FAILED.value
    assert result["message"] == "Filename is not found"
    gateway.message_publisher.assert_called_once()
    logger.error.assert_called_once()

def test_execute_success_flow():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()

    gateway.storage_build_path.return_value = "/tmp/file.jpg"
    gateway.image_preprocessor.return_value = "img"
    gateway.ocr_processor.return_value = [
        {"text": "ABC"},
        {"text": "1234"}
    ]

    result = usecase.execute(
        message={"filename": "file.jpg"},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    assert result["status"] == CaptureStatus.COMPLETED.value
    assert result["result"] == "ABC1234"
    assert result["result_status"] is True
    assert gateway.message_publisher.call_count == 3

def test_execute_exception_flow():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()
    gateway.storage_build_path.side_effect = Exception("boom")

    result = usecase.execute(
        message={"filename": "file.jpg"},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    assert result["status"] == CaptureStatus.FAILED.value
    assert "boom" in str(result["message"])

    gateway.message_publisher.assert_called()
    logger.error.assert_called()

def test_message_publisher_called_with_started_status():
    usecase = CaptureUseCaseImpl()
    gateway = Mock()
    logger = Mock()
    gateway.storage_build_path.return_value = "/tmp/file.jpg"
    gateway.image_preprocessor.return_value = "img"
    gateway.ocr_processor.return_value = [{"text": "A"}]

    usecase.execute(
        message={"filename": "file.jpg"},
        gateway=gateway,
        connection=Mock(),
        exchange="ex",
        routing_key="rk",
        logger=logger,
        storage="/tmp"
    )

    first_call_args = gateway.message_publisher.call_args_list[0].kwargs["message"]
    assert first_call_args["status"] == CaptureStatus.STARTED.value