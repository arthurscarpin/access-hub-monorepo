from typing import Any

import pytest
import numpy as np
from unittest.mock import Mock

from infrastructure.ocr.easyocr_processor import EasyOCRProcessor


def test_execute_none_image():
    processor = EasyOCRProcessor()

    with pytest.raises(ValueError) as exc:
        processor.execute(None)

    assert "Image cannot be None" in str(exc.value)

def test_execute_calls_easyocr_reader(monkeypatch: Any):
    fake_reader = Mock()
    fake_reader.readtext.return_value = []
    monkeypatch.setattr("easyocr.Reader", lambda *args, **kwargs: fake_reader)
    processor = EasyOCRProcessor()
    image = np.zeros((10, 10), dtype=np.uint8)

    processor.execute(image)

    fake_reader.readtext.assert_called_once()

def test_execute_transforms_output(monkeypatch: Any):
    fake_reader = Mock()
    fake_reader.readtext.return_value = [
        ([[0, 0], [1, 0], [1, 1], [0, 1]], "ABC123", 0.9),
    ]
    monkeypatch.setattr("easyocr.Reader", lambda *args, **kwargs: fake_reader)
    processor = EasyOCRProcessor()
    image = np.zeros((10, 10), dtype=np.uint8)

    result = processor.execute(image)

    assert len(result) == 1
    assert result[0]["text"] == "ABC123"
    assert result[0]["confidence"] == 0.9

def test_execute_ignores_empty_text(monkeypatch: Any):
    fake_reader = Mock()
    fake_reader.readtext.return_value = [
        ([[0, 0], [1, 0], [1, 1], [0, 1]], "", 0.8),
        ([[0, 0], [1, 0], [1, 1], [0, 1]], "OK", 0.9),
    ]
    monkeypatch.setattr("easyocr.Reader", lambda *args, **kwargs: fake_reader)
    processor = EasyOCRProcessor()
    image = np.zeros((10, 10), dtype=np.uint8)

    result = processor.execute(image)

    assert len(result) == 1
    assert result[0]["text"] == "OK"

def test_execute_sorting(monkeypatch: Any):
    fake_reader = Mock()
    fake_reader.readtext.return_value = [
        ([[10, 50], [20, 50], [20, 60], [10, 60]], "B", 0.9),  # mais baixo
        ([[10, 10], [20, 10], [20, 20], [10, 20]], "A", 0.9),  # mais alto
    ]
    monkeypatch.setattr("easyocr.Reader", lambda *args, **kwargs: fake_reader)
    processor = EasyOCRProcessor()
    image = np.zeros((10, 10), dtype=np.uint8)

    result = processor.execute(image)
    
    assert result[0]["text"] == "A"
    assert result[1]["text"] == "B"