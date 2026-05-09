from pathlib import Path


class Storage:

    def __init__(self, root: Path, folder: str = "storage"):
        self._root = root
        self._folder = folder

    def build(self, filename: str) -> str:
        return str(self._root / self._folder / filename)