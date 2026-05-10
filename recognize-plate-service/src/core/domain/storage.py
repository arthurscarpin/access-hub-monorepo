from pathlib import Path

class Storage:

    def __init__(self, storage: str):
        if not storage:
            raise ValueError("STORAGE_PATH is required")
        self._root = Path(storage).resolve()

    def build(self, filename: str) -> str:
        return str(self._root / filename)