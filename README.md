# Access Control System Monorepo

Distributed access-control platform for vehicle entry and exit validation. The monorepo combines an Angular frontend, a Spring Boot backend, a Python OCR worker, and a Python plate-intelligence worker to process image captures asynchronously and produce auditable access decisions.

The system is designed around service isolation: the frontend provides the operational UI, the backend owns business rules and persistence, the OCR service extracts text from images, and the intelligence service reconstructs the final Brazilian license plate from OCR candidates.

## Services

| Service | Runtime | Responsibility | Documentation |
| --- | --- | --- | --- |
| `frontend-service` | Angular 21, Nginx | Browser UI for authentication, dashboard, captures, users, owners, vehicles, scopes, and access events | [frontend-service/README.md](frontend-service/README.md) |
| `backend-service` | Java 17, Spring Boot | REST API, authentication, authorization, owners, vehicles, captures, access events, persistence, RabbitMQ orchestration | [backend-service/README.md](backend-service/README.md) |
| `recognize-plate-service` | Python, OpenCV, EasyOCR | Consumes OCR image jobs, pre-processes images, extracts OCR candidates, publishes image status updates | [recognize-plate-service/README.md](recognize-plate-service/README.md) |
| `plate-intelligence-service` | Python, LangChain, OpenAI | Consumes completed capture analysis jobs, ranks OCR candidates, reconstructs final plate, publishes AI validation results | [plate-intelligence-service/README.md](plate-intelligence-service/README.md) |

## Platform Architecture

```mermaid
flowchart LR
    Browser[Browser] -->|HTTP| Frontend[frontend-service]
    Frontend -->|Static Angular app| Browser
    Browser -->|HTTP REST| Backend[backend-service]

    Backend -->|PostgreSQL tables| Postgres[(PostgreSQL)]
    Backend -->|Capture documents| Mongo[(MongoDB)]
    Backend -->|Publish OCR jobs| Rabbit[(RabbitMQ Topic Exchange)]

    Rabbit -->|OCR queue| OCR[recognize-plate-service]
    OCR -->|OCR status updates| Rabbit

    Rabbit -->|AI validation queue| Intelligence[plate-intelligence-service]
    Intelligence -->|AI result messages| Rabbit

    Backend <-->|Consume status and result queues| Rabbit
    Backend -->|Final access event| Postgres

    Storage[(Shared image storage)] --> OCR
```

## Capture Processing Flow

```mermaid
sequenceDiagram
    participant Client
    participant Backend as backend-service
    participant RabbitMQ
    participant OCR as recognize-plate-service
    participant AI as plate-intelligence-service
    participant DB as PostgreSQL/MongoDB

    Client->>Backend: POST /captures/upload with ZIP filename and direction
    Backend->>Backend: Validate and extract ZIP from storage
    Backend->>DB: Persist capture document with extracted images
    Backend->>RabbitMQ: Publish one OCR job per extracted image

    RabbitMQ->>OCR: Deliver OCR image job
    OCR->>RabbitMQ: Publish STARTED status
    OCR->>OCR: Load image and run OpenCV pre-processing
    OCR->>OCR: Run EasyOCR extraction
    OCR->>RabbitMQ: Publish COMPLETED or FAILED status

    RabbitMQ->>Backend: Deliver OCR status update
    Backend->>DB: Update image and capture status

    Backend->>RabbitMQ: Publish AI validation job when all images finish
    RabbitMQ->>AI: Deliver capture with OCR candidates
    AI->>AI: Aggregate candidates and reconstruct final plate
    AI->>RabbitMQ: Publish final plate result

    RabbitMQ->>Backend: Deliver AI result
    Backend->>DB: Update capture and create access event
```

## Access Decision Flow

```mermaid
flowchart TD
    FinalPlate[Final plate from AI result] --> Lookup{Vehicle exists?}
    Lookup -- No --> Denied[Create DENIED access event]
    Lookup -- Yes --> Status{Vehicle status}
    Status -- ACTIVE --> Authorized[Create AUTHORIZED access event]
    Status -- BLOCKED --> Denied
    Authorized --> History[Access history queryable by API]
    Denied --> History
```

## Main Capabilities

- JWT-secured REST API with OAuth2 scopes and method-level authorization.
- Angular frontend served by Nginx in production Docker images.
- Docker Compose orchestration for databases, RabbitMQ, backend, workers, and frontend.
- Owner, vehicle, user, scope, capture, and access-event management.
- PostgreSQL persistence with Flyway migrations.
- ZIP-based capture ingestion with storage backup/error handling.
- MongoDB capture storage for extracted image OCR state and AI result metadata.
- RabbitMQ-based asynchronous orchestration between backend, OCR, and AI services, with retry and dead-letter handling for failed messages.
- OpenCV-based plate image pre-processing.
- EasyOCR text extraction with bounding boxes and confidence scores.
- LangChain/OpenAI plate reconstruction for Brazilian Mercosul and legacy formats.
- Test coverage and quality gates across Java and Python services.
- GitHub Actions pipelines with coverage, SonarCloud, and CodeQL.

## Repository Layout

```text
.
+-- backend-service
|   +-- src
|   +-- postman
|   +-- pom.xml
|   +-- Dockerfile
|   +-- README.md
+-- frontend-service
|   +-- src
|   +-- public
|   +-- package.json
|   +-- Dockerfile
|   +-- nginx.conf
|   +-- README.md
+-- recognize-plate-service
|   +-- src
|   +-- test
|   +-- pyproject.toml
|   +-- Dockerfile
|   +-- README.md
+-- plate-intelligence-service
|   +-- src
|   +-- test
|   +-- pyproject.toml
|   +-- Dockerfile
|   +-- README.md
+-- storage
+-- docker-compose.yaml
+-- .github
    +-- workflows
```

## Messaging Overview

| Flow | Producer | Consumer | Purpose |
| --- | --- | --- | --- |
| OCR request | `backend-service` | `recognize-plate-service` | Sends one image-processing job per capture image. |
| OCR status update | `recognize-plate-service` | `backend-service` | Reports image status and extracted OCR candidates. |
| AI validation request | `backend-service` | `plate-intelligence-service` | Sends completed capture OCR data for final plate reconstruction. |
| AI validation result | `plate-intelligence-service` | `backend-service` | Returns the final plate, confidence, reasoning, and processing status. |

## Retry and Dead-Letter Handling

RabbitMQ failures are handled at the queue boundary:

- `backend-service` declares the topic exchange, a derived dead-letter exchange named `${RABBITMQ_EXCHANGE}.dlx`, the main queues, and one `.dlq` queue per main queue.
- Backend consumers use Spring AMQP stateless retry with `3` attempts and exponential backoff from `1000ms` to `10000ms`.
- Python workers retry failed message processing using `RABBITMQ_MAX_RETRIES` and `RABBITMQ_BASE_DELAY_SECONDS`, republishing the message with an `x-retry-count` header.
- Messages that still fail after retries are negatively acknowledged without requeue and are routed to the corresponding dead-letter queue.

| Main queue | Dead-letter queue |
| --- | --- |
| OCR request queue | `${RABBITMQ_OCR_QUEUE}.dlq` |
| OCR status queue | `${RABBITMQ_OCR_STATUS_QUEUE}.dlq` |
| AI validation queue | `${RABBITMQ_AI_VALIDATION_QUEUE}.dlq` |
| AI result queue | `${RABBITMQ_AI_RESULT_QUEUE}.dlq` |

## Persistence Overview

```mermaid
flowchart TB
    Backend[backend-service]

    Backend --> Owners[(owner)]
    Backend --> Vehicles[(vehicle)]
    Backend --> Users[(users)]
    Backend --> Scopes[(scopes)]
    Backend --> UserScopes[(users_scopes)]
    Backend --> Events[(access_event)]
    Backend --> Captures[(MongoDB captures)]

    Owners --> Vehicles
    Users --> UserScopes
    Scopes --> UserScopes
```

## CI/CD Overview

```mermaid
flowchart LR
    Changes[Changes under a service folder] --> Workflow[Service GitHub Actions workflow]
    Workflow --> Tests[Run tests and coverage]
    Tests --> Artifacts[Upload coverage or build artifacts]
    Artifacts --> Sonar[SonarCloud analysis]
    Workflow --> CodeQL[CodeQL security analysis]
```

| Service | Workflow | Quality Steps |
| --- | --- | --- |
| `backend-service` | `.github/workflows/backend-service-ci-cd.yaml` | Maven `clean verify`, JaCoCo, JAR artifact upload, SonarCloud, CodeQL Java |
| `recognize-plate-service` | `.github/workflows/recognize-plate-service-ci-cd.yaml` | `uv sync`, pytest coverage, SonarCloud, CodeQL Python |
| `plate-intelligence-service` | `.github/workflows/plate-intelligence-service.yml` | `uv sync`, pytest coverage, SonarCloud, CodeQL Python |

## Getting Started

Each service has its own runtime, dependencies, environment variables, and Docker instructions. Start with the service-specific documentation:

1. [frontend-service](frontend-service/README.md)
2. [backend-service](backend-service/README.md)
3. [recognize-plate-service](recognize-plate-service/README.md)
4. [plate-intelligence-service](plate-intelligence-service/README.md)

### Docker Compose

The repository root includes `docker-compose.yaml` for the full stack:

```bash
docker compose up --build
```

The Compose setup builds and starts:

- PostgreSQL on `localhost:5432`;
- MongoDB on `localhost:27017`;
- RabbitMQ AMQP on `localhost:5672`;
- RabbitMQ Management UI on `localhost:15672`;
- backend API on `localhost:8080`;
- frontend UI on `localhost:4200`;
- OCR and plate-intelligence workers.

The frontend image is built from `frontend-service/Dockerfile`. It compiles Angular with the production configuration and serves the generated `dist/frontend-service/browser` files through Nginx.

Recommended manual startup order for a full local environment:

1. Start PostgreSQL, MongoDB, RabbitMQ, and shared image storage.
2. Start `backend-service`.
3. Start `recognize-plate-service`.
4. Start `plate-intelligence-service`.
5. Start `frontend-service` or use the backend Swagger UI/Postman collection to create users, owners, vehicles, and captures.

## Documentation Map

- Frontend UI, Angular scripts, Docker/Nginx image, and Compose usage: [frontend-service/README.md](frontend-service/README.md)
- Backend API, data model, scopes, Docker, and Java CI/CD: [backend-service/README.md](backend-service/README.md)
- OCR worker, OpenCV/EasyOCR pipeline, message contract, Docker, and Python CI/CD: [recognize-plate-service/README.md](recognize-plate-service/README.md)
- AI plate reconstruction worker, LangChain/OpenAI prompt flow, message contract, Docker, and Python CI/CD: [plate-intelligence-service/README.md](plate-intelligence-service/README.md)
