from pathlib import Path

class Storage:

    def __init__(self, folder: str = "storage"):
        self._root = Path(__file__).resolve().parents[4]
        self._folder = folder

    def build(self, filename: str) -> str:
        return str(self._root / self._folder / filename)