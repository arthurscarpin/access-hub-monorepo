from enum import Enum

class ImageStatus(Enum):
    STARTED = "STARTED"
    PROCESSING = "PROCESSING"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"

class CaptureStatus(Enum):
    PROCESSING = "PROCESSING"