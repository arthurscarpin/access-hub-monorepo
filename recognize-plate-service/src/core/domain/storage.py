from pathlib import Path

class Storage:

    def __init__(self, storage: str = None, **kwargs):
        self._monorepo_root = Path(__file__).resolve().parents[4]

    def build(self, filename: str) -> str:
        if not filename:
            raise ValueError("Filename cannot be empty")
            
        clean_filename = filename.strip("/")
        absolute_path = self._monorepo_root / clean_filename
        return str(absolute_path)