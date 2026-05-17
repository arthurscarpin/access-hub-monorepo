import pytest
from pathlib import Path
from typing import Any

from core.domain.storage import Storage


def test_init_with_valid_storage(tmp_path: Path) -> None:
    storage = Storage(str(tmp_path))
    assert isinstance(storage.root, Path)
    assert storage.root == tmp_path.resolve()

def test_init_with_empty_storage():
    with pytest.raises(ValueError) as exc:
        Storage("")
    assert str(exc.value) == "STORAGE_PATH is required"

def test_build_simple_filename(tmp_path: Path):
    storage = Storage(str(tmp_path))
    result = storage.build("file.txt")
    expected = str(tmp_path.resolve() / "file.txt")
    assert result == expected

def test_build_with_subdirectory(tmp_path: Path):
    storage = Storage(str(tmp_path))
    result = storage.build("folder/file.txt")
    expected = str(tmp_path.resolve() / "folder/file.txt")
    assert result == expected

def test_build_with_filename_prefixed_by_storage_root(tmp_path: Path):
    storage_root = tmp_path / "storage"
    storage = Storage(str(storage_root))

    result = storage.build("storage/tmp/file.txt")

    expected = str(storage_root.resolve() / "tmp/file.txt")
    assert result == expected

def test_build_with_absolute_filename(tmp_path: Path):
    storage = Storage(str(tmp_path))
    absolute_path = "/tmp/other/file.txt"
    result = storage.build(absolute_path)
    expected = str(Path(absolute_path))
    assert result == expected

def test_root_is_resolved(tmp_path: Path):
    relative_path: Any = tmp_path.relative_to(tmp_path.parent)
    storage = Storage(str(relative_path))
    assert storage.root.is_absolute()
