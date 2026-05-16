# Plate Intelligence Service

Python worker responsible for reconstructing and validating Brazilian license plates from OCR candidates. The service consumes completed capture payloads from RabbitMQ, aggregates OCR results from multiple images, asks an OpenAI-powered LangChain assistant to produce the final plate decision, and publishes the enriched capture result back to the access-control backend.

This service is the intelligence layer of the access-control platform. It does not expose an HTTP API; it operates as a message-driven process designed for asynchronous, scalable, and isolated plate-analysis workloads.

## Highlights

- Message-driven Python worker built around RabbitMQ and durable JSON messages.
- Clean separation between core use case contracts and infrastructure adapters.
- Deterministic OCR candidate aggregation before invoking the LLM.
- Brazilian plate support for Mercosul (`ABC1D23`) and legacy (`ABC1234`) formats.
- LangChain integration with OpenAI chat models and structured JSON output parsing.
- Dedicated prompt engineering for positional OCR correction and plate reconstruction.
- Explicit success and failure publishing contract for backend orchestration.
- Structured logging with production-aware console output and local NDJSON files.
- Automated tests covering the use case, consumer, producer, OpenAI assistant wrapper, and candidate aggregation.
- CI pipeline with pytest coverage, SonarCloud analysis, and CodeQL security scanning.

## Role in the Platform

The access-control platform splits plate processing into independent services:

1. The backend service receives a capture and stores image metadata.
2. OCR workers extract text candidates from each image.
3. The backend publishes a capture to the AI validation queue when OCR processing is complete.
4. Plate Intelligence consumes that capture, reconstructs the most likely plate, and publishes the final result.
5. The backend receives the result, updates the capture, and creates the access event.

This design keeps OCR, AI reasoning, and access-control business rules independently deployable.

## Processing Flow

```text
RabbitMQ AI validation queue
        |
        v
PlateConsumer
        |
        v
ValidatePlateUseCaseUseCaseImpl
        |
        +-- PlateCandidateAggregator
        |      - normalizes OCR text
        |      - detects Mercosul and legacy formats
        |      - counts repeated candidates
        |      - ranks by valid format, frequency, and confidence
        |
        +-- OpenAIAssistant through LangChain
        |      - applies the plate reconstruction prompt
        |      - returns structured JSON
        |
        v
PlateResultProducer
        |
        v
RabbitMQ AI result routing key
```

## Architecture

```text
src
+-- core
|   +-- gateway         # Abstract contract for plate analysis and publishing
|   +-- usecase         # Plate validation orchestration
+-- infrastructure
    +-- ai_assistants   # OpenAI/LangChain assistant and system prompt
    +-- configuration   # Settings, RabbitMQ, ChatOpenAI, logging
    +-- consumer        # RabbitMQ consumer
    +-- gateway         # Concrete analysis gateway
    +-- parsers         # Pydantic output parser
    +-- plate_analysis  # OCR candidate aggregation
    +-- producer        # RabbitMQ result producer
```

The `core` package defines the application behavior and gateway contract. The `infrastructure` package provides RabbitMQ, OpenAI, LangChain, logging, parsing, and analysis implementations.

## Technology Stack

| Area | Technology |
| --- | --- |
| Runtime | Python 3.12 |
| Dependency management | uv |
| Messaging | RabbitMQ, pika |
| AI orchestration | LangChain |
| LLM provider | OpenAI |
| Data validation | Pydantic, pydantic-settings |
| Configuration | Environment variables, `.env`, `.env.idea` |
| Testing | pytest, pytest-cov |
| Packaging | Docker |
| CI quality gates | GitHub Actions, SonarCloud, CodeQL |

## Message Contract

### Input

The service expects a capture payload from the RabbitMQ AI validation queue. The relevant fields are:

```json
{
  "id": "capture-id",
  "images": [
    {
      "id": "image-id",
      "filename": "plate-frame-001.jpg",
      "status": "COMPLETED",
      "ocr": [
        {
          "text": "R102A19",
          "confidence": 0.91,
          "bbox": [[10, 20], [120, 20], [120, 60], [10, 60]]
        }
      ]
    }
  ]
}
```

The worker reads every `images[].ocr[]` entry, normalizes each candidate, and builds a ranked analysis report for the AI assistant.

### Successful Output

On success, the service publishes:

```json
{
  "capture": {
    "id": "capture-id",
    "status": "COMPLETED",
    "finalPlate": "RIO2A19",
    "finalConfidence": 0.94,
    "reasoning": "OCR candidates point to RIO2A19 after positional correction."
  },
  "error": null
}
```

### Failure Output

If validation fails, the service marks the capture as failed and publishes the error details:

```json
{
  "capture": {
    "id": "capture-id",
    "status": "FAILED"
  },
  "error": "error message"
}
```

The consumer acknowledges valid messages after the use case completes. Invalid JSON or unexpected processing failures are negatively acknowledged without requeue.

## Plate Reconstruction Rules

The service combines deterministic pre-processing with LLM-based final reconstruction:

- OCR candidates are normalized to uppercase alphanumeric strings.
- Candidates shorter than three characters are ignored.
- Mercosul plates must match `LLLNLNN`.
- Legacy plates must match `LLLNNNN`.
- Candidate ranking prioritizes valid plate format, repeated occurrences, and highest confidence.
- The system prompt forces positional OCR correction for common confusions such as `0` vs `O`, `1` vs `I`, `5` vs `S`, and `8` vs `B`.
- The final assistant response must follow the structured parser with `final_plate`, `final_confidence`, and `reasoning`.

## Configuration

Settings are loaded from environment variables. When present, `.env.idea` is used before `.env`.

| Variable | Purpose |
| --- | --- |
| `ENVIRONMENT` | Runtime environment. Non-production environments also write local NDJSON logs. |
| `RABBITMQ_HOST` | RabbitMQ host. |
| `RABBITMQ_PORT` | RabbitMQ port. |
| `RABBITMQ_USERNAME` | RabbitMQ username. |
| `RABBITMQ_PASSWORD` | RabbitMQ password. |
| `RABBITMQ_EXCHANGE` | RabbitMQ exchange used to publish results. |
| `RABBITMQ_AI_VALIDATION_QUEUE` | Queue consumed by this service. |
| `RABBITMQ_AI_RESULT_ROUTING_KEY` | Routing key used to publish AI validation results. |
| `OPENAI_API_KEY` | OpenAI API key used by LangChain. |
| `LANGCHAIN_DEBUG` | Enables LangChain debug output when `true`. |
| `LLM_MODEL` | OpenAI model name used for plate reconstruction. |
| `TEMPERATURE` | LLM temperature. Use low values for deterministic reconstruction. |
| `MAX_TOKENS` | Maximum completion tokens for the LLM response. |

Example `.env`:

```dotenv
ENVIRONMENT=dev
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_EXCHANGE=access-control.exchange
RABBITMQ_AI_VALIDATION_QUEUE=capture.ai.validation
RABBITMQ_AI_RESULT_ROUTING_KEY=capture.ai.result
OPENAI_API_KEY=sk-...
LANGCHAIN_DEBUG=false
LLM_MODEL=gpt-4o-mini
TEMPERATURE=0
MAX_TOKENS=500
```

## Running Locally

### Prerequisites

- Python 3.12
- uv
- RabbitMQ
- OpenAI API key

### Install Dependencies

```bash
uv sync
```

### Run the Worker

```bash
uv run python src/main.py
```

The process blocks while listening to the configured RabbitMQ queue.

## Docker

Build the image:

```bash
docker build -t plate-intelligence-service .
```

Run the worker with environment variables:

```bash
docker run --rm --env-file .env plate-intelligence-service
```

The Dockerfile uses `python:3.12-slim`, installs `uv`, syncs locked production dependencies, and starts `src/main.py`.

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

- OCR candidate normalization, aggregation, ranking, and format detection.
- Plate validation use case success and failure publishing paths.
- RabbitMQ consumer acknowledgment and negative acknowledgment behavior.
- RabbitMQ result producer payload encoding and delivery properties.
- OpenAI assistant chain invocation and error propagation.

## CI/CD

The GitHub Actions workflow for this service runs when files under `plate-intelligence-service/**` change. It includes:

- dependency synchronization with `uv sync --frozen --no-dev`;
- pytest coverage generation;
- coverage artifact upload;
- SonarCloud analysis;
- CodeQL security analysis for Python.

## Operational Notes

- This worker requires RabbitMQ to be reachable before startup.
- The service creates a dedicated channel for consuming and a producer channel when publishing results.
- Message prefetch is set to `1` to process one AI validation task at a time per worker instance.
- Published messages use JSON content type and delivery mode `2` for persistence.
- In non-production environments, logs are also written to `logs/YYYYMMDD.ndjson`.
- Horizontal scaling is achieved by running multiple worker instances against the same queue.
