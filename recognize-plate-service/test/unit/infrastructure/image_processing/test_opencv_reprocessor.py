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

def test_execute_finds_plate_and_crops(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "plate.jpg"
    image = np.zeros((500, 500, 3), dtype=np.uint8)
    cv2.rectangle(image, (10, 10), (210, 60), (255, 255, 255), -1)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert isinstance(result, np.ndarray)
    assert result.shape[0] > 80 

def test_execute_contour_with_invalid_aspect_ratio(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "square.jpg"
    image = np.zeros((500, 500, 3), dtype=np.uint8)
    cv2.rectangle(image, (50, 50), (150, 150), (255, 255, 255), -1)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert result.shape[0] == 1000

def test_execute_small_contours_ignored(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "small.jpg"
    image = np.zeros((500, 500, 3), dtype=np.uint8)
    cv2.rectangle(image, (10, 10), (30, 20), (255, 255, 255), -1)
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert result.shape[0] == 1000

def test_execute_non_quadrilateral_contours(tmp_path: Path):
    processor = OpenCVPreProcessor()
    img_path = tmp_path / "triangle.jpg"
    image = np.zeros((500, 500, 3), dtype=np.uint8)
    pts = np.array([[10,10], [100,10], [50,100]], np.int32)
    cv2.fillPoly(image, [pts], (255, 255, 255))
    cv2.imwrite(str(img_path), image)
    result = processor.execute(str(img_path))
    assert result is not None