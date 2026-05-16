# Access Control System API

Production-oriented backend service for vehicle access control, built with Spring Boot and a clean domain-first architecture. The service manages owners, vehicles, users, authorization scopes, access history, and an asynchronous image-capture pipeline that integrates OCR and AI validation through RabbitMQ.

The project was designed as more than a CRUD API: it combines relational consistency, document persistence, event-driven processing, JWT-based authorization, database migrations, observability endpoints, API documentation, and automated quality gates.

## Highlights

- Domain-driven application structure with separated core use cases, gateways, infrastructure adapters, and presentation controllers.
- Stateless authentication with JWT, RSA key pairs, BCrypt password hashing, and method-level authorization through OAuth2 scopes.
- PostgreSQL persistence for owners, vehicles, users, scopes, and access events, versioned with Flyway migrations.
- MongoDB persistence for multi-image capture workflows and OCR/AI processing state.
- RabbitMQ topic exchange for asynchronous capture processing, OCR status updates, AI validation requests, and AI result consumption.
- OpenAPI and Swagger UI documentation exposed by the application.
- Centralized exception handling with structured error responses.
- Automated test suite covering domain rules, use cases, mappers, controllers, producers, consumers, and integration behavior.
- JaCoCo coverage gate configured at 80% instruction coverage during Maven verification.

## Core Capabilities

### Identity and Authorization

- Register users with explicit scope assignments.
- Authenticate users through `/login` and receive short-lived JWT access tokens.
- Protect sensitive routes with bearer tokens and fine-grained scopes such as `vehicle:write`, `owner:write`, `access_event:read`, and `capture:write`.
- Expose available scopes through `/scopes` to support administrative user provisioning.

### Vehicle and Owner Management

- Register owners with normalized and validated Brazilian document types (`CPF` and `RG`).
- Register vehicles linked to owners.
- Normalize vehicle plates before persistence and validation.
- Toggle vehicle status between `ACTIVE` and `BLOCKED`.
- Use active vehicle status as the business rule for access authorization.

### Access Event Processing

- Store access events with plate, timestamp, direction, and result.
- Authorize access when the detected plate belongs to an active vehicle.
- Deny access when the vehicle does not exist or is blocked.
- Query access history with optional filters by plate and time range, using pageable responses.

### Capture, OCR, and AI Pipeline

The capture workflow is asynchronous and message-driven:

1. A client submits one or more image filenames to `/captures`.
2. The service stores a capture document in MongoDB.
3. One RabbitMQ message is published for each image to request OCR processing.
4. OCR workers publish image-level status updates back to the service.
5. When every image reaches a terminal state, the service publishes the capture to the AI validation queue.
6. The AI result consumer receives the final plate decision.
7. The service updates the capture and creates the corresponding access event.

This design keeps HTTP requests fast while allowing OCR and AI processing to scale independently.

## Architecture

```text
src/main/java/com/arthurscarpin/acs
+-- core
|   +-- accessevent     # Access validation and history use cases
|   +-- capture         # Capture workflow domain and orchestration
|   +-- owner           # Owner registration and document validation
|   +-- scope           # Authorization scope lookup
|   +-- user            # User registration and authentication
|   +-- vehicle         # Vehicle registration and status rules
+-- infrastructure
    +-- configuration   # Spring, security, OpenAPI, JPA, MongoDB, RabbitMQ
    +-- gateway         # Persistence and messaging adapters
    +-- mapper          # MapStruct DTO/entity/domain mapping
    +-- persistence     # JPA entities, MongoDB documents, repositories
    +-- presentation    # REST controllers, consumers, producers, advice, DTOs
```

The `core` package contains business rules and gateway contracts. The `infrastructure` package adapts those contracts to Spring MVC, JPA, MongoDB, RabbitMQ, security, and OpenAPI.

## Technology Stack

| Area | Technology |
| --- | --- |
| Runtime | Java 17 |
| Framework | Spring Boot 4 |
| HTTP API | Spring Web MVC |
| Security | Spring Security, OAuth2 Resource Server, JWT, RSA keys, BCrypt |
| Relational database | PostgreSQL |
| Database migrations | Flyway |
| Document database | MongoDB |
| Messaging | RabbitMQ, Spring AMQP |
| Mapping | MapStruct |
| API documentation | Springdoc OpenAPI / Swagger UI |
| Observability | Spring Boot Actuator, structured Logback encoder |
| Tests | JUnit, Spring Boot Test, Testcontainers |
| Coverage | JaCoCo |
| Packaging | Maven, Docker |

## API Surface

| Method | Path | Description | Authentication |
| --- | --- | --- | --- |
| `POST` | `/users` | Register a user with scopes | Public |
| `POST` | `/login` | Authenticate and issue a JWT | Public |
| `GET` | `/scopes` | List available authorization scopes | Public |
| `POST` | `/owners` | Register an owner | Bearer token + `owner:write` or admin |
| `POST` | `/vehicles` | Register a vehicle | Bearer token + `vehicle:write` or admin |
| `PATCH` | `/vehicles/{id}` | Toggle vehicle status | Bearer token + `vehicle:write` or admin |
| `GET` | `/access-events` | Query access history with filters and pagination | Bearer token + `access_event:read` or admin |
| `POST` | `/captures` | Register a capture and publish OCR jobs | Bearer token + `capture:write` or admin |
| `GET` | `/actuator/health` | Health check | Public |
| `GET` | `/api/api-docs` | OpenAPI JSON | Public |
| `GET` | `/swagger/index.html` | Swagger UI | Public |

## Authorization Scopes

The initial Flyway seed creates these scopes:

- `admin:all`
- `vehicle:write`
- `vehicle:read`
- `owner:write`
- `owner:read`
- `access_event:write`
- `access_event:read`
- `capture:write`
- `capture:read`

## Configuration

The active Spring profile is read from the `ENVIRONMENT` variable:

```bash
export ENVIRONMENT=prod
```

### Required Production Variables

| Variable | Purpose |
| --- | --- |
| `POSTGRES_HOST` | PostgreSQL host. Defaults to `postgres` in the production profile. |
| `POSTGRES_PORT` | PostgreSQL port. |
| `POSTGRES_DB` | PostgreSQL database name. |
| `POSTGRES_USER` | PostgreSQL username. |
| `POSTGRES_PASSWORD` | PostgreSQL password. |
| `MONGO_HOST` | MongoDB host. |
| `MONGO_PORT` | MongoDB port. |
| `MONGO_DB` | MongoDB database name. |
| `MONGO_USER` | MongoDB username. |
| `MONGO_PASSWORD` | MongoDB password. |
| `MONGO_AUTHENTICATION` | MongoDB authentication database. |
| `RABBITMQ_HOST` | RabbitMQ host. Defaults to `rabbitmq` in the production profile. |
| `RABBITMQ_PORT` | RabbitMQ port. |
| `RABBITMQ_USERNAME` | RabbitMQ username. |
| `RABBITMQ_PASSWORD` | RabbitMQ password. |
| `RABBITMQ_VIRTUAL_HOST` | RabbitMQ virtual host. |
| `RABBITMQ_EXCHANGE` | Topic exchange used by the capture pipeline. |
| `RABBITMQ_OCR_ROUTING_KEY` | Routing key for OCR requests. |
| `RABBITMQ_OCR_QUEUE` | Queue consumed by OCR workers. |
| `RABBITMQ_OCR_STATUS_ROUTING_KEY` | Routing key for OCR status updates. |
| `RABBITMQ_OCR_STATUS_QUEUE` | Queue consumed by this service for OCR status updates. |
| `RABBITMQ_AI_VALIDATION_ROUTING_KEY` | Routing key for AI validation requests. |
| `RABBITMQ_AI_VALIDATION_QUEUE` | Queue consumed by AI validation workers. |
| `RABBITMQ_AI_RESULT_ROUTING_KEY` | Routing key for AI validation results. |
| `RABBITMQ_AI_RESULT_QUEUE` | Queue consumed by this service for AI results. |

## JWT Key Pair

The application expects RSA keys on the classpath:

- `src/main/resources/authz.pem`
- `src/main/resources/authz.pub`

Generate a development key pair with:

```bash
# Private key
openssl genpkey -algorithm RSA -out src/main/resources/authz.pem -pkeyopt rsa_keygen_bits:2048

# Public key
openssl rsa -pubout -in src/main/resources/authz.pem -out src/main/resources/authz.pub
```

For production deployments, inject keys through a secure secret-management process instead of committing private keys.

## Running Locally

### Prerequisites

- Java 17
- Maven wrapper support (`./mvnw`)
- PostgreSQL
- MongoDB
- RabbitMQ

### Build

```bash
./mvnw clean package
```

### Run

```bash
export ENVIRONMENT=prod
export POSTGRES_HOST=localhost
export POSTGRES_PORT=5432
export POSTGRES_DB=access_control
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
export MONGO_HOST=localhost
export MONGO_PORT=27017
export MONGO_DB=access_control
export MONGO_USER=mongo
export MONGO_PASSWORD=mongo
export MONGO_AUTHENTICATION=admin
export RABBITMQ_HOST=localhost
export RABBITMQ_PORT=5672
export RABBITMQ_USERNAME=guest
export RABBITMQ_PASSWORD=guest
export RABBITMQ_VIRTUAL_HOST=/
export RABBITMQ_EXCHANGE=access-control.exchange
export RABBITMQ_OCR_ROUTING_KEY=capture.ocr.requested
export RABBITMQ_OCR_QUEUE=capture.ocr.processing
export RABBITMQ_OCR_STATUS_ROUTING_KEY=capture.ocr.updated
export RABBITMQ_OCR_STATUS_QUEUE=capture.ocr.status
export RABBITMQ_AI_VALIDATION_ROUTING_KEY=capture.ai.validation
export RABBITMQ_AI_VALIDATION_QUEUE=capture.ai.validation
export RABBITMQ_AI_RESULT_ROUTING_KEY=capture.ai.result
export RABBITMQ_AI_RESULT_QUEUE=capture.ai.result

./mvnw spring-boot:run
```

The service starts on the default Spring Boot port:

```text
http://localhost:8080
```

Useful local URLs:

- Swagger UI: `http://localhost:8080/swagger/index.html`
- OpenAPI JSON: `http://localhost:8080/api/api-docs`
- Health check: `http://localhost:8080/actuator/health`

## Docker

Build the application JAR before creating the image:

```bash
./mvnw clean package
docker build -t access-control-system-api .
```

Run the container with the same environment variables required by the production profile:

```bash
docker run --rm -p 8080:8080 --env-file .env access-control-system-api
```

## Testing and Quality

Run the automated test suite:

```bash
./mvnw test
```

Run verification with the JaCoCo coverage gate:

```bash
./mvnw verify
```

The Maven configuration enforces a minimum instruction coverage ratio of `0.80` during `verify`.

## CI/CD

The GitHub Actions workflow for this service runs when files under `backend-service/**` change. It includes:

- Java 17 setup with Amazon Corretto and Maven dependency caching;
- environment provisioning from GitHub Actions variables and secrets;
- JWT key generation for test, SonarCloud, and CodeQL execution contexts;
- Maven `clean verify` with test execution and JaCoCo coverage validation;
- build artifact upload for the generated backend JAR;
- JaCoCo report upload for downstream quality analysis;
- SonarCloud analysis using compiled Java bytecode and the JaCoCo XML report;
- CodeQL security analysis for Java.

## Postman Collection

A Postman collection is available at:

```text
postman/Access Control System.postman_collection.json
```

It includes request examples for users, authentication, scopes, owners, vehicles, access events, and captures.

## Database Model

Flyway migrations create and seed:

- `owner`
- `vehicle`
- `access_event`
- `users`
- `scopes`
- `users_scopes`
- PostgreSQL `pgcrypto` extension for UUID generation

MongoDB stores capture documents, including image-level OCR status, OCR results, final plate decision, confidence, reasoning, processing timestamps, and versioning metadata.

## Operational Notes

- The API is stateless; authenticated requests must include `Authorization: Bearer <token>`.
- Token expiration is currently configured by the login use case as `600` seconds.
- Flyway runs automatically and validates the relational schema on startup.
- RabbitMQ queues and bindings are declared by the application at startup.
- Capture processing depends on external OCR and AI workers that publish and consume the configured RabbitMQ messages.
