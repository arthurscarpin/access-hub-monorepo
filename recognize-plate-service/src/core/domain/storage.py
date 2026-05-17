from pathlib import Path

class Storage:

    def __init__(self, storage: str = None, **kwargs):
        if not storage:
            raise ValueError("STORAGE_PATH is required")
        self.root = Path(storage).expanduser().resolve()

    def build(self, filename: str) -> str:
        if not filename:
            raise ValueError("Filename cannot be empty")

        path = Path(filename)
        if path.is_absolute():
            return str(path)

        clean_filename = Path(filename.strip("/"))
        storage_path = Path(str(self.root).strip("/"))

        if storage_path.parts and clean_filename.parts[:len(storage_path.parts)] == storage_path.parts:
            return str(Path("/") / clean_filename)

        if clean_filename.parts and clean_filename.parts[0] == self.root.name:
            return str(self.root.parent / clean_filename)

        return str(self.root / clean_filename)
