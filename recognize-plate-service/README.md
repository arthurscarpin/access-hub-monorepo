# Recognize Plate Service

Python worker responsible for OCR extraction in the access-control platform. The service consumes image-processing jobs from RabbitMQ, loads the referenced image from local/shared storage, applies OpenCV pre-processing, extracts text candidates with EasyOCR, and publishes image-level status updates back to the backend.

This service does not expose an HTTP API. It is a message-driven OCR worker designed to run independently from the backend and the AI plate-reconstruction service.

## Highlights

- RabbitMQ worker for asynchronous license-plate OCR processing with bounded retries before dead-letter routing.
- Three-stage status publishing: `STARTED`, `PROCESSING`, and `COMPLETED` or `FAILED`.
- OpenCV image pre-processing with contour-based plate crop detection.
- EasyOCR text extraction configured for Portuguese OCR.
- Durable JSON result publishing with OCR text, confidence, and bounding boxes.
- Clean separation between core use case contracts and infrastructure adapters.
- Storage abstraction for resolving image filenames into filesystem paths.
- Production-aware logging with local NDJSON logs outside production.
- Automated unit tests covering storage, use case flow, consumer behavior, publisher behavior, OpenCV pre-processing, and EasyOCR output mapping.
- CI pipeline with pytest coverage, SonarCloud analysis, and CodeQL security scanning.

## Role in the Platform

The access-control platform processes captures across specialized services:

1. The backend receives a capture request and publishes one OCR job per image.
2. Recognize Plate Service consumes each OCR job.
3. The service loads and pre-processes the target image.
4. EasyOCR extracts text candidates and confidence scores.
5. The service publishes image status updates back to RabbitMQ.
6. The backend aggregates OCR status for all images in the capture.
7. When all images are processed, the backend sends the capture to the Plate Intelligence Service for final plate reconstruction.

This split keeps OCR workloads isolated and horizontally scalable.

## Processing Flow

```text
RabbitMQ OCR queue
        |
        v
CaptureConsumer
        |
        v
CaptureUseCaseImpl
        |
        +-- publish STARTED status
        |
        +-- Storage
        |      - resolves STORAGE_PATH + filename
        |
        +-- publish PROCESSING status
        |
        +-- OpenCVPreProcessor
        |      - loads image
        |      - detects likely plate contour
        |      - crops plate area when possible
        |      - converts to grayscale
        |      - resizes, denoises, and applies CLAHE
        |
        +-- EasyOCRProcessor
        |      - extracts text candidates
        |      - converts bounding boxes to JSON-safe coordinates
        |      - sorts OCR items by screen position
        |
        +-- publish COMPLETED or FAILED status
        |
        v
RabbitMQ OCR status routing key
```

## Architecture

```text
src
+-- core
|   +-- domain          # Status enums and storage path builder
|   +-- gateway         # Abstract capture processing gateway
|   +-- usecase         # OCR processing orchestration
+-- infrastructure
    +-- configuration   # Settings, RabbitMQ, logging
    +-- consumer        # RabbitMQ capture consumer
    +-- gateway         # Concrete storage, pre-processing, OCR, publishing adapter
    +-- image_processing # OpenCV image pre-processing
    +-- ocr             # EasyOCR processor
    +-- producer        # RabbitMQ OCR status producer
```

The `core` package owns the processing workflow and domain contracts. The `infrastructure` package provides RabbitMQ integration, filesystem path resolution, OpenCV processing, EasyOCR execution, and result publishing.

## Technology Stack

| Area | Technology |
| --- | --- |
| Runtime | Python 3.11+ and < 3.13 |
| Dependency management | uv |
| Messaging | RabbitMQ, pika |
| OCR engine | EasyOCR |
| Image processing | OpenCV headless, NumPy |
| ML runtime | torch, torchvision |
| Configuration | Pydantic Settings, `.env`, `.env.idea` |
| Testing | pytest, pytest-cov |
| Packaging | Docker |
| CI quality gates | GitHub Actions, SonarCloud, CodeQL |

## Message Contract

### Input

The service consumes OCR jobs from the configured RabbitMQ OCR queue. The relevant fields are:

```json
{
  "captureId": "capture-id",
  "imageId": "image-id",
  "filename": "storage/tmp/capture-id/image-id.jpg",
  "timestamp": "2026-01-01T00:00:00Z"
}
```

The `filename` field is required. It is resolved against `STORAGE_PATH` before image loading. The worker accepts filenames relative to the storage root, such as `tmp/capture-id/image-id.jpg`, and backend-published paths prefixed with the storage directory, such as `storage/tmp/capture-id/image-id.jpg`.

### Started Status

Published before filesystem and OCR processing begin:

```json
{
  "captureId": "capture-id",
  "imageId": "image-id",
  "filename": "vehicle-frame-001.jpg",
  "image_status": "STARTED",
  "capture_status": "PROCESSING",
  "message": "Execution started",
  "ocr": []
}
```

### Processing Status

Published after the image path is resolved and before OCR execution:

```json
{
  "captureId": "capture-id",
  "imageId": "image-id",
  "filename": "vehicle-frame-001.jpg",
  "image_status": "PROCESSING",
  "capture_status": "PROCESSING",
  "message": "Execution in processing...",
  "ocr": []
}
```

### Completed Status

Published when OCR extraction succeeds:

```json
{
  "captureId": "capture-id",
  "imageId": "image-id",
  "filename": "vehicle-frame-001.jpg",
  "image_status": "COMPLETED",
  "capture_status": "PROCESSING",
  "message": "Execution completed",
  "ocr": [
    {
      "text": "ABC1234",
      "confidence": 0.91,
      "bbox": [[10, 20], [120, 20], [120, 60], [10, 60]]
    }
  ]
}
```

### Failed Status

Published when the filename is missing or image/OCR processing raises an exception:

```json
{
  "captureId": "capture-id",
  "imageId": "image-id",
  "image_status": "FAILED",
  "capture_status": "PROCESSING",
  "message": "Filename is not found",
  "ocr": []
}
```

The consumer acknowledges valid messages after the use case completes. Invalid JSON is negatively acknowledged without requeue. Unexpected processing failures are retried with an `x-retry-count` header before the message is negatively acknowledged without requeue, allowing RabbitMQ to route it to the OCR queue DLQ configured by the backend.

### Retry Behavior

For unexpected processing failures, the worker:

- reads the current retry count from the `x-retry-count` message header;
- acknowledges the failed delivery before scheduling the retry;
- waits `RABBITMQ_BASE_DELAY_SECONDS ** retry_count` seconds;
- republishes the original message to the configured OCR status routing key with an incremented `x-retry-count`;
- negatively acknowledges without requeue when `RABBITMQ_MAX_RETRIES` is reached.

## Image Processing

The OpenCV pre-processor applies a practical plate-focused pipeline:

- loads the image from the resolved storage path;
- converts the image to grayscale;
- applies bilateral filtering and Canny edge detection;
- searches the largest contours for a quadrilateral with plate-like aspect ratio;
- crops the detected plate region when a candidate is found;
- falls back to the full image when no plate contour is detected;
- converts the crop to grayscale, scales it by `2x`, denoises it, and applies CLAHE.

The OCR processor then uses EasyOCR to return JSON-safe candidates containing:

- `text`: extracted text;
- `confidence`: OCR confidence as a float;
- `bbox`: bounding box coordinates as integer lists.

## Configuration

Settings are loaded from environment variables. When present, `.env.idea` is used before `.env`.

| Variable | Purpose |
| --- | --- |
| `ENVIRONMENT` | Runtime environment. Non-production environments also write local NDJSON logs. |
| `RABBITMQ_HOST` | RabbitMQ host. |
| `RABBITMQ_PORT` | RabbitMQ port. |
| `RABBITMQ_USERNAME` | RabbitMQ username. |
| `RABBITMQ_PASSWORD` | RabbitMQ password. |
| `RABBITMQ_EXCHANGE` | RabbitMQ exchange used to publish status updates. |
| `RABBITMQ_OCR_QUEUE` | Queue consumed by this service. |
| `RABBITMQ_OCR_STATUS_ROUTING_KEY` | Routing key used to publish OCR status updates. |
| `RABBITMQ_AI_VALIDATION_ROUTING_KEY` | Platform routing key required by the current settings model. |
| `RABBITMQ_MAX_RETRIES` | Maximum number of processing retries before the message is rejected without requeue. |
| `RABBITMQ_BASE_DELAY_SECONDS` | Base used to calculate retry delay as `base ** retry_count` seconds. |
| `STORAGE_PATH` | Root directory where image filenames are resolved. In Docker Compose this is usually `storage`, mounted as `/app/storage`. |

Example `.env`:

```dotenv
ENVIRONMENT=dev
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_EXCHANGE=access-control.exchange
RABBITMQ_OCR_QUEUE=capture.ocr.processing
RABBITMQ_OCR_STATUS_ROUTING_KEY=capture.ocr.updated
RABBITMQ_AI_VALIDATION_ROUTING_KEY=capture.ai.validation
RABBITMQ_MAX_RETRIES=3
RABBITMQ_BASE_DELAY_SECONDS=2
STORAGE_PATH=/data/access-control/images
```

## Running Locally

### Prerequisites

- Python 3.11 or 3.12
- uv
- RabbitMQ
- Local or shared image storage mounted at `STORAGE_PATH`

### Install Dependencies

```bash
uv sync
```

### Run the Worker

```bash
uv run python src/main.py
```

The process blocks while listening to the configured OCR queue.

## Docker

Build the image:

```bash
docker build -t recognize-plate-service .
```

Run the worker with environment variables and a mounted storage directory:

```bash
docker run --rm \
  --env-file .env \
  -v /host/images:/data/access-control/images \
  recognize-plate-service
```

The Dockerfile uses `python:3.11-slim`, installs the native libraries required by OpenCV, syncs locked dependencies with `uv`, and starts `src/main.py`.

## Testing and Quality

Run the test suite:

```bash
uv run pytest
```

Run tests with coverage using the project configuration:

```bash
uv run pytest --cov=src --cov-report=term --cov-config=.coveragerc
```

The current tests cover:

- storage path validation and construction;
- success, missing filename, and exception flows in the capture use case;
- RabbitMQ consumer acknowledgment and negative acknowledgment behavior;
- RabbitMQ retry behavior with `x-retry-count` before DLQ routing;
- OCR status producer payload encoding and delivery properties;
- OpenCV pre-processing behavior for valid, invalid, and contour edge cases;
- EasyOCR processor output transformation, empty-text filtering, and sorting.

## CI/CD

The GitHub Actions workflow for this service runs when files under `recognize-plate-service/**` change. It includes:

- dependency synchronization with `uv sync --frozen --no-dev`;
- pytest coverage generation;
- coverage artifact upload;
- SonarCloud analysis;
- CodeQL security analysis for Python.

## Operational Notes

- This worker requires RabbitMQ and image storage to be available before processing jobs.
- Message prefetch is set to `1` to process one image at a time per worker instance.
- Processing failures are retried up to `RABBITMQ_MAX_RETRIES`; after that the message is rejected without requeue and should land in `${RABBITMQ_OCR_QUEUE}.dlq` when the backend-declared topology is active.
- Published messages use JSON content type and delivery mode `2` for persistence.
- EasyOCR is initialized with `gpu=True`; runtime environments should provide compatible GPU support or adjust the processor configuration when running CPU-only deployments.
- `STORAGE_PATH` must point to the same storage location where the backend or capture ingestion flow writes image files. With the monorepo Docker Compose, both backend and OCR worker mount repository-root `./storage` at `/app/storage`.
- In non-production environments, logs are also written to `logs/YYYYMMDD.ndjson`.
- Horizontal scaling is achieved by running multiple worker instances against the same OCR queue.
