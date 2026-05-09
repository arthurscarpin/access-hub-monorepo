from enum import Enum


class RoutingKeyEnum(str, Enum):
    OCR_REQUESTED = "capture.ocr.requested"
    FAILED = "ocr.status.failed"
    STARTED = "ocr.status.started"
    PROCESSING = "ocr.status.processing"
    COMPLETED = "ocr.status.completed"

class ExchangeEnum(str, Enum):
    CAPTURE_EVENTS = "acs.capture.events"
    OCR_STATUS_EVENTS = "acs.ocr.status.events"
    OCR_DLX = "acs.ocr.dlx"