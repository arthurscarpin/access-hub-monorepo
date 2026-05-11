import pytest
import numpy as np
import cv2
from pathlib import Path

from infrastructure.image_processing.opencv_preprocessor import OpenCVPreProcessor

def test_execute_invalid_image_path():
    processor = OpenCVPreProcessor()
    with pytest.raises(ValueError) as exc:
        processor.execute("invalid_path.jpg")
    assert "Não foi possível carregar a imagem" in str(exc.value)

def test_execute_valid_image(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "img.jpg"
    image = np.zeros((200, 400, 3), dtype=np.uint8)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert isinstance(result, np.ndarray)
    assert result.ndim == 2
    assert result.size > 0

def test_execute_returns_grayscale_image(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "img.jpg"
    image = np.zeros((200, 400, 3), dtype=np.uint8)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert result.dtype == np.uint8
    assert len(result.shape) == 2

def test_execute_never_returns_none(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "img.jpg"
    image = np.zeros((200, 400, 3), dtype=np.uint8)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert result is not None