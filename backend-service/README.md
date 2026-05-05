# Access Control System - Backend Service

<div align="center">

[![Java Version](https://img.shields.io/badge/Java-17-ED8936?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

A robust, scalable REST API backend for an **Access Control System** built with Spring Boot. This microservice manages user authentication, vehicle registration, owner management, and access event tracking with JWT-based security.

[Features](#features) • [Quick Start](#quick-start) • [API Documentation](#api-documentation) • [Architecture](#architecture) • [Contributing](#contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Development Tools & IDE Setup](#-development-tools--ide-setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database](#database)
- [Security](#security)
- [Testing](#testing)
- [Build & Deployment](#build--deployment)
- [Environment Variables](#environment-variables)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

The **Access Control System Backend Service** is a comprehensive REST API built with Spring Boot that manages access control workflows. It provides secure endpoints for:

- **User Authentication**: JWT-based authentication with login functionality
- **User Management**: User registration and profile management
- **Owner Management**: Owner/company registration and management
- **Vehicle Management**: Vehicle registration, tracking, and status updates
- **Access Event Tracking**: Real-time access validation and historical records

The service uses PostgreSQL for persistent data storage, JWT tokens for security, and follows Clean Architecture principles with clear separation of concerns (domain, use cases, and infrastructure layers).

---

## ✨ Features

### Core Features
- ✅ **JWT Authentication** - Secure token-based authentication with public/private key pairs
- ✅ **User Registration & Login** - Complete user management with email validation
- ✅ **Owner Management** - Register and manage owners/companies
- ✅ **Vehicle Management** - Register vehicles with license plates and status tracking
- ✅ **Access Event Tracking** - Real-time access validation and comprehensive history
- ✅ **Role-Based Access Control** - Fine-grained permission management with custom annotations

### Technical Features
- 🏗️ **Clean Architecture** - Domain-driven design with clear separation of layers
- 🔒 **Spring Security** - OAuth2 Resource Server with JWT validation
- 💾 **Database Migrations** - Flyway for version-controlled schema management
- 📊 **Pagination Support** - Efficient data retrieval with Spring Data JPA pagination
- 📝 **OpenAPI/Swagger** - Auto-generated API documentation with SpringDoc
- 📋 **Structured Logging** - JSON-formatted logs with Logstash integration
- 🧪 **Comprehensive Testing** - 20+ unit and integration tests with MockMvc and Testcontainers
- 🗺️ **Entity Mapping** - MapStruct for efficient DTO/Domain entity conversion
- 📦 **Reduced Boilerplate** - Lombok annotations for cleaner code
- 🐳 **Docker Support** - Production-ready containerization

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

| Component | Version | Download |
|-----------|---------|----------|
| **Java Development Kit (JDK)** | 17+ | [OpenJDK](https://adoptopenjdk.net/) |
| **Maven** | 3.8+ | [Maven](https://maven.apache.org/) or use `./mvnw` |
| **PostgreSQL** | 16+ | [PostgreSQL](https://www.postgresql.org/) |
| **Docker** (optional) | Latest | [Docker](https://www.docker.com/) |
| **Docker Compose** (optional) | Latest | Included with Docker Desktop |

### System Requirements
- **Memory**: Minimum 512 MB (2 GB recommended)
- **Disk Space**: Minimum 500 MB for the service and dependencies
- **Network**: Open port 8080 (API) and 5432 (PostgreSQL)

---

## 🚀 Installation

### Option 1: Local Installation

#### 1. Clone the Repository
```bash
git clone https://github.com/arthurscarpin/access-control-system.git
cd access-control-system/backend-service
```

#### 2. Build the Project
```bash
# Using Maven wrapper (Windows)
./mvnw.cmd clean package

# Or using system Maven
mvn clean package
```

#### 3. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or using the compiled JAR
java -jar target/access-control-system-0.0.1-SNAPSHOT.jar
```

### Option 2: Docker Installation

#### Using Docker Compose (Recommended for Development)

1. Navigate to the project root:
```bash
cd access-control-system
```

2. Create a `.env` file with required variables:
```bash
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=access_control_db
POSTGRES_USER=admin
POSTGRES_PASSWORD=securepassword123
JWT_PUBLIC_KEY=your_jwt_public_key
JWT_PRIVATE_KEY=your_jwt_private_key
```

3. Start the services:
```bash
docker-compose up -d
```

4. Verify the services are running:
```bash
docker-compose ps
```

#### Using Docker CLI

```bash
# Build the image
docker build -f backend-service/Dockerfile -t access-control-system:latest .

# Run the container
docker run -d \
  --name backend-service \
  -p 8080:8080 \
  -e POSTGRES_HOST=postgres \
  -e POSTGRES_DB=access_control_db \
  access-control-system:latest
```

---

## ⚡ Quick Start

### 1. Start PostgreSQL
```bash
# If using Docker Compose from project root
docker-compose up -d postgres

# Or start PostgreSQL locally/manually
```

### 2. Configure Environment Variables
Create `application-local.yaml` in `src/main/resources/`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/access_control_db
    username: admin
    password: securepassword123
```

### 3. Build and Run
```bash
./mvnw clean package spring-boot:run
```

### 4. Access the Application
- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger/index.html`
- **API Docs**: `http://localhost:8080/api/api-docs`

### 5. Test the Service
```bash
# Health check
curl http://localhost:8080/actuator/health

# Login example
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

---

## 🛠️ Development Tools & IDE Setup

### IDE Configuration

#### IntelliJ IDEA (Recommended)

1. **Open Project**:
   - File → Open → Select `access-control-system`
   - Trust the project when prompted

2. **Install Lombok Plugin**:
   - File → Settings → Plugins
   - Search "Lombok" → Install "Lombok" plugin
   - Restart IDE

3. **Enable Annotation Processing**:
   - File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"
   - Check "Obtain processors from project classpath"

4. **Configure Running Tests**:
   - Run → Edit Configurations
   - Create JUnit configuration for running test suites
   - Set VM options: `-Dspring.datasource.url=...` (optional)

5. **Use Built-in Terminal**:
   - View → Tool Windows → Terminal
   - Run Maven commands directly

#### VS Code/Visual Studio Code

1. **Install Extensions**:
   - Extension Pack for Java (Microsoft)
   - Debugger for Java (Microsoft)
   - Maven for Java (Microsoft)
   - Spring Boot Extension Pack (VMware)
   - Lombok Annotations Support for VS Code

2. **Workspace Settings** (`.vscode/settings.json`):
```json
{
    "java.compile.nullAnalysis.mode": "automatic",
    "maven.terminal.useJavaHome": true,
    "[java]": {
        "editor.defaultFormatter": "redhat.java",
        "editor.formatOnSave": true
    }
}
```

3. **Debug Configuration** (`.vscode/launch.json`):
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Spring Boot App",
            "type": "java",
            "name": "Spring Boot (Main)",
            "request": "launch",
            "mainClass": "com.arthurscarpin.acs.AccessControlSystemApplication",
            "args": ""
        }
    ]
}
```

#### Eclipse

1. **Install Lombok**:
   - Help → Eclipse Marketplace
   - Search "Lombok" → Install Lombok IDE

2. **Configure Maven**:
   - Right-click project → Maven → Update Project
   - Check "Force Update of Snapshots/Releases"

3. **Build Project**:
   - Right-click project → Maven → Clean
   - Right-click project → Maven → Install

### Useful IDE Shortcuts

| Action | IntelliJ | VS Code | Eclipse |
|--------|----------|---------|---------|
| Run Tests | Ctrl+Shift+F10 | Ctrl+F5 | Ctrl+F11 |
| Debug Tests | Ctrl+Shift+F9 | F5 | Ctrl+Shift+F11 |
| Search Symbol | Ctrl+Alt+Shift+N | Ctrl+F12 | Ctrl+O |
| Format Code | Ctrl+Alt+L | Shift+Alt+F | Ctrl+Shift+F |
| Quick Fix | Alt+Enter | Ctrl+. | Ctrl+1 |
| Generate Code | Alt+Insert | Ctrl+. | Alt+Insert |

### Development Workflow

1. **Create Feature Branch**:
```bash
git checkout -b feature/your-feature-name
```

2. **Make Changes**:
   - Edit domain models in `core/*/domain/`
   - Add use cases in `core/*/usecase/`
   - Create mappers in `infrastructure/mapper/`
   - Add controllers in `infrastructure/presentation/controller/`

3. **Write Tests**:
   - Unit tests: `src/test/java/core/*/`
   - Integration tests: `src/test/java/infrastructure/presentation/`
   - Follow naming convention: `*Test.java` or `*Tests.java`

4. **Run Locally**:
```bash
# Start PostgreSQL
docker-compose up -d postgres

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

5. **Verify API**:
   - Open http://localhost:8080/swagger/index.html
   - Test endpoints directly
   - Check logs for errors

6. **Commit & Push**:
```bash
git add .
git commit -m "feat: add your feature description"
git push origin feature/your-feature-name
```

### Debugging Tips

#### Debug Java Application in IDE

```bash
# IntelliJ: Select "Debug" from run configurations
# OR manually start with debug options
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
  -jar target/access-control-system-0.0.1-SNAPSHOT.jar
```

#### View SQL Queries

Enable in `application.yaml`:
```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
```

#### Enable Debug Logging

```bash
# Via environment variable
LOGGING_LEVEL_ROOT=DEBUG mvn spring-boot:run

# Or in application.yaml
logging:
  level:
    root: DEBUG
    com.arthurscarpin.acs: DEBUG
    org.hibernate.SQL: DEBUG
```

#### Connect to Running Application

```bash
# Attach debugger to running app on port 5005
# In IntelliJ: Run → Attach to Process... or Edit Configs → Remote
```

---

## ⚙️ Configuration

### Application Properties

The service is configured via `application.yaml`. Key configurations include:

```yaml
spring:
  application:
    name: access-control-system
  
  # Database Configuration
  datasource:
    url: jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
    driver-class-name: org.postgresql.Driver
  
  # JPA/Hibernate Configuration
  jpa:
    open-in-view: false
    show-sql: true
    hibernate:
      ddl-auto: validate  # Prevents auto-generation, uses Flyway migrations
    properties:
      hibernate:
        format_sql: true
  
  # Flyway Database Migration
  flyway:
    enabled: true
  
  # Swagger/OpenAPI Configuration
  springdoc:
    api-docs:
      path: /api/api-docs
    swagger-ui:
      path: /swagger/index.html

# JWT Configuration
jwt:
  public:
    key: classpath:authz.pub
  private:
    key: classpath:authz.pem
```

### Project Dependencies

#### Core Framework
- **Spring Boot 4.0.6**: Modern Java framework with embedded Tomcat
- **Spring Data JPA**: Object-relational mapping with Hibernate
- **Spring Security & OAuth2**: Authentication and authorization
- **Spring Validation**: Input validation annotations

#### Database
- **PostgreSQL Driver**: PostgreSQL 16+ support
- **Flyway**: Database migration management

#### Mapping & Code Generation
- **MapStruct 1.6.3**: Type-safe entity mapping with compile-time code generation
- **Lombok**: Reduces boilerplate with annotations (`@Getter`, `@Setter`, `@Builder`)

#### API Documentation
- **SpringDoc OpenAPI 2.8.0**: Generates Swagger UI and OpenAPI spec
- **Swagger UI**: Interactive API documentation at `/swagger/index.html`

#### Logging
- **Logstash Logback Encoder 7.4**: JSON-formatted structured logging

#### Testing
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Testcontainers 1.19.3**: PostgreSQL container for integration tests
- **Spring Test & MockMvc**: REST endpoint testing

### Active Profiles

Run with specific profiles for different environments:

```bash
# Development (local database, debug logging)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production (production database, info logging)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Or set environment variable
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Maven Build Configuration

The project uses Maven with specialized plugins:

```bash
# Build phases
mvn clean              # Clean previous builds
mvn compile            # Compile source code
mvn test               # Run all tests
mvn verify             # Run integration tests
mvn package            # Create executable JAR
mvn install            # Install to local repository

# Common combinations
mvn clean package              # Clean build and package
mvn clean package -DskipTests  # Package without tests
mvn clean verify               # Full build with all tests
```

### MapStruct Configuration

MapStruct automatically generates mapping code during compilation:

```xml
<!-- In pom.xml: annotation processors -->
<annotationProcessorPaths>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
```

**Create custom mappers**:

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromEntityToDomain(UserEntity entity);
    
    @Mapping(source = "id", target = "userId")
    UserResponse fromDomainToResponse(User user);
    
    List<UserResponse> fromDomainListToResponseList(List<User> users);
}
```

### Lombok Configuration

Lombok generates getters, setters, constructors, and builders:

```java
@Getter                    // Generates all getters
@Setter                    // Generates all setters
@NoArgsConstructor         // Generates no-arg constructor
@AllArgsConstructor        // Generates all-args constructor
@Builder                   // Generates builder pattern
@ToString                  // Generates toString()
@EqualsAndHashCode         // Generates equals() and hashCode()
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;
    
    private String email;
    private String password;
}
```

---

## 📚 API Documentation

### Interactive API Docs
Access the Swagger UI for interactive API exploration:
```
http://localhost:8080/swagger/index.html
```

### API Endpoints Overview

#### Authentication Endpoints

##### Login
```http
POST /login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

#### User Endpoints

##### Register User
```http
POST /users
Content-Type: application/json
Authorization: Bearer {token}

{
  "email": "newuser@example.com",
  "password": "securepass123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Owner Endpoints

##### Register Owner
```http
POST /owners
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "Company Name",
  "document": "12345678000195",
  "documentType": "CNPJ"
}
```

#### Vehicle Endpoints

##### Register Vehicle
```http
POST /vehicles
Content-Type: application/json
Authorization: Bearer {token}

{
  "plate": "ABC-1234",
  "ownerId": "owner-uuid",
  "model": "Toyota Corolla",
  "color": "Silver"
}
```

##### Get All Vehicles (with Pagination)
```http
GET /vehicles?page=0&size=10
Authorization: Bearer {token}
```

##### Update Vehicle Status
```http
PATCH /vehicles/{vehicleId}/status
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "ACTIVE"
}
```

#### Access Event Endpoints

##### Validate Access
```http
POST /access-events
Content-Type: application/json
Authorization: Bearer {token}

{
  "plate": "ABC-1234",
  "direction": "ENTRY",
  "timestamp": "2026-05-04T10:30:00Z"
}

Response: 201 Created
{
  "id": "event-uuid",
  "plate": "ABC-1234",
  "direction": "ENTRY",
  "result": "ALLOWED",
  "timestamp": "2026-05-04T10:30:00Z"
}
```

##### Get Access History
```http
GET /access-events?plate=ABC-1234&from=2026-05-01T00:00:00Z&to=2026-05-04T23:59:59Z&page=0&size=20
Authorization: Bearer {token}
```

### Error Responses

The API returns standard HTTP status codes with detailed error messages:

```json
{
  "timestamp": "2026-05-04T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/access-events"
}
```

| Status | Description |
|--------|-------------|
| `200` | Success |
| `201` | Created |
| `400` | Bad Request (validation error) |
| `401` | Unauthorized (missing/invalid token) |
| `403` | Forbidden (insufficient permissions) |
| `404` | Not Found |
| `409` | Conflict (resource already exists) |
| `500` | Internal Server Error |

---

## 🏗️ Architecture

### Clean Architecture Layers

The project is organized following **Clean Architecture** principles with strict separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│              REST Controllers (Presentation)             │
│  (Receives HTTP requests, validates input, returns DTOs) │
└───────────────────┬─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│        Entity Mappers (Infrastructure Layer)             │
│  (MapStruct: DTOs ↔ Entities, Entities ↔ Domain Models) │
└───────────────────┬─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│        Use Cases / Business Logic (Core Layer)           │
│  (Orchestrates domain entities and gateways)             │
└───────────────────┬─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│        Domain Models & Value Objects (Core Layer)        │
│  (Business rules, validations, enums)                    │
└───────────────────┬─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│        Gateway Interfaces (Core Layer Contract)          │
│  (Defines contracts for data access)                     │
└───────────────────┬─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│        Repository Implementations (Infrastructure)       │
│  (JPA repositories, database operations)                 │
└─────────────────────────────────────────────────────────┘
```

### Key Design Patterns

| Pattern | Implementation | Purpose |
|---------|---|---------|
| **Use Case Pattern** | `*UseCaseImpl` classes | Encapsulates business logic and orchestrates domain entities |
| **Repository Pattern** | `*Repository` interfaces + `*RepositoryGateway` | Abstracts data access from business logic |
| **Mapper Pattern** | `*Mapper` (MapStruct) | Automatically converts between DTOs, entities, and domain models |
| **Gateway Pattern** | `*Gateway` interfaces | Defines contracts for external dependencies |
| **Dependency Injection** | Spring IoC Container | Loose coupling and testability |
| **Value Objects** | `CPF`, `Plate`, `Name` records | Ensures domain validation at the model level |
| **Domain Events** | `AccessEvent`, `AccessResult` | Models domain concepts with clear semantics |
| **Exception Handling** | Global `@ControllerAdvice` | Consistent error responses across the API |

### Entity-to-Domain Mapping with MapStruct

MapStruct automatically generates efficient mapping code between layers:

```java
// JPA Entity (Infrastructure Layer)
@Entity
@Table(name = "users")
public class UserEntity {
    private UUID id;
    private String email;
    private String password;
    private List<ScopeEntity> scopes;
}

// Domain Model (Core Layer)
public record User(
    UUID id,
    String name,
    String email,
    String password,
    boolean active,
    List<UUID> scopes
) {}

// Automatic Mapping (Infrastructure Layer)
@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromEntityToDomain(UserEntity entity);
    UserEntity fromDomainToEntity(User domain);
    UserResponse fromDomainToResponse(User domain);
}
```

**Benefits of MapStruct**:
- 🚀 Compile-time code generation (no reflection)
- 🔧 Zero runtime overhead
- 📝 Type-safe mapping with compiler checks
- 🧵 Automatic null handling
- 🔗 Custom mapping methods when needed

### Data Flow Example: User Login

```
1. Client sends POST /login with email/password
          ↓
2. LoginController receives and validates LoginRequest DTO
          ↓
3. LoginMapper converts LoginRequest → User domain model
          ↓
4. LoginUserUseCase executes business logic:
   - Verifies user exists (UserGateway)
   - Validates password (LoginGateway)
   - Retrieves scopes (ScopeGateway)
          ↓
5. LoginGateway generates JWT token
          ↓
6. Response mapper converts domain model → LoginResponse DTO
          ↓
7. Controller returns 200 OK with token and expiration
```

### Domain Models (Value Objects)

**Validation at Model Layer**:

```java
// Plate Domain Model - Automatic validation
public class Plate {
    private final String plate;
    
    public Plate(String plate) {
        if(!isValidPlate(plate)) {
            throw new InvalidPlateException("Invalid plate format");
        }
        this.plate = plate;
    }
    
    private boolean isValidPlate(String plate) {
        // Business logic for plate validation
        return Pattern.matches("[A-Z]{3}-\\d{4}", plate);
    }
}

// CPF Domain Model
public class CPF implements Document {
    private final String value;
    
    public CPF(String value) {
        if(!isValidCPF(value)) {
            throw new InvalidCPFException("Invalid CPF format");
        }
        this.value = value;
    }
    
    @Override
    public String getFormatted() {
        return String.format("%s.%s.%s-%s", 
            value.substring(0, 3),
            value.substring(3, 6),
            value.substring(6, 9),
            value.substring(9));
    }
}
```

### Scope/RBAC Architecture

Scopes enable fine-grained permission management:

```
User → has many → Scope (via users_scopes junction table)
  
Scopes available:
- admin:all (full access)
- vehicle:write (create/update vehicles)
- vehicle:read (view vehicles)
- owner:write (manage owners)
- owner:read (view owners)
- access_event:write (record access events)
- access_event:read (view access history)

JWT Token includes user's scopes
Controllers validate scopes before processing
```

---

## 📁 Project Structure

```
backend-service/
├── src/
│   ├── main/
│   │   ├── java/com/arthurscarpin/acs/
│   │   │   ├── core/                          # Enterprise Business Rules Layer
│   │   │   │   ├── accessevent/              # Access event domain logic
│   │   │   │   │   ├── domain/
│   │   │   │   │   │   ├── AccessEvent.java
│   │   │   │   │   │   ├── Direction.java
│   │   │   │   │   │   └── AccessResult.java
│   │   │   │   │   ├── usecase/
│   │   │   │   │   │   └── ValidateAccessUseCaseImpl.java
│   │   │   │   │   └── gateway/
│   │   │   │   ├── owner/                    # Owner domain logic
│   │   │   │   │   ├── domain/
│   │   │   │   │   │   ├── DocumentFactory.java
│   │   │   │   │   │   ├── CPF.java
│   │   │   │   │   │   ├── Name.java
│   │   │   │   │   │   └── Owner.java
│   │   │   │   │   └── usecase/
│   │   │   │   ├── scope/                    # RBAC scope management
│   │   │   │   │   ├── domain/Scope.java
│   │   │   │   │   └── gateway/ScopeGateway.java
│   │   │   │   ├── user/                     # User domain logic
│   │   │   │   │   ├── domain/User.java
│   │   │   │   │   ├── usecase/
│   │   │   │   │   │   ├── RegisterUserUseCaseImpl.java
│   │   │   │   │   │   └── LoginUserUseCaseImpl.java
│   │   │   │   │   ├── exception/
│   │   │   │   │   └── gateway/
│   │   │   │   ├── vehicle/                  # Vehicle domain logic
│   │   │   │   │   ├── domain/
│   │   │   │   │   │   ├── Vehicle.java
│   │   │   │   │   │   ├── Plate.java
│   │   │   │   │   │   └── VehicleStatus.java
│   │   │   │   │   ├── usecase/
│   │   │   │   │   └── gateway/
│   │   │   │   └── pagination/               # Pagination utilities
│   │   │   │
│   │   │   └── infrastructure/               # External Frameworks & DB Layer
│   │   │       ├── configuration/            # Spring configurations
│   │   │       │   └── SecurityConfiguration.java
│   │   │       ├── gateway/                  # Repository implementations
│   │   │       │   ├── UserRepositoryGateway.java
│   │   │       │   ├── VehicleRepositoryGateway.java
│   │   │       │   ├── ScopeRepositoryGateway.java
│   │   │       │   └── AccessEventRepositoryGateway.java
│   │   │       ├── mapper/                   # MapStruct entity mappers
│   │   │       │   ├── UserMapper.java
│   │   │       │   ├── VehicleMapper.java
│   │   │       │   ├── ScopeMapper.java
│   │   │       │   ├── LoginMapper.java
│   │   │       │   ├── AccessEventMapper.java
│   │   │       │   └── OwnerMapper.java
│   │   │       ├── persistence/              # JPA repositories & entities
│   │   │       │   ├── entity/
│   │   │       │   │   ├── UserEntity.java
│   │   │       │   │   ├── VehicleEntity.java
│   │   │       │   │   ├── ScopeEntity.java
│   │   │       │   │   ├── OwnerEntity.java
│   │   │       │   │   └── AccessEventEntity.java
│   │   │       │   └── repository/
│   │   │       │       ├── UserRepository.java
│   │   │       │       ├── VehicleRepository.java
│   │   │       │       ├── ScopeRepository.java
│   │   │       │       ├── OwnerRepository.java
│   │   │       │       └── AccessEventRepository.java
│   │   │       └── presentation/             # REST controllers & DTOs
│   │   │           ├── controller/
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── UserController.java
│   │   │           │   ├── VehicleController.java
│   │   │           │   ├── OwnerController.java
│   │   │           │   └── AccessEventController.java
│   │   │           ├── request/              # Request DTOs
│   │   │           ├── response/             # Response DTOs
│   │   │           ├── advice/               # Global exception handling
│   │   │           └── documentation/        # OpenAPI specs
│   │   │
│   │   └── resources/
│   │       ├── application.yaml              # Main configuration
│   │       ├── db/migration/                 # Flyway SQL migrations (V0-V7)
│   │       ├── authz.pem & authz.pub         # JWT RSA key pair
│   │       └── logback-spring.xml            # Logging configuration
│   │
│   └── test/
│       └── java/com/arthurscarpin/acs/
│           ├── AccessControlSystemIntegrationTest.java  # Base integration test
│           ├── core/
│           │   ├── accessevent/
│           │   ├── owner/
│           │   │   └── domain/
│           │   │       ├── CPFTest.java
│           │   │       ├── NameTest.java
│           │   │       ├── RGTest.java
│           │   │       └── DocumentFactoryTest.java
│           │   ├── user/
│           │   │   └── usecase/
│           │   │       ├── LoginUserUseCaseImplTest.java
│           │   │       └── RegisterUserUseCaseImplTest.java
│           │   └── vehicle/
│           │       └── usecase/
│           │           ├── RegisterVehicleUseCaseImplTest.java
│           │           └── UpdateVehicleStatusUseCaseImplTest.java
│           └── infrastructure/
│               ├── mapper/                   # MapStruct mapper tests
│               │   ├── UserMapperTest.java
│               │   ├── VehicleMapperTest.java
│               │   ├── ScopeMapperTest.java
│               │   ├── LoginMapperTest.java
│               │   ├── AccessEventMapperTest.java
│               │   └── OwnerMapperTest.java
│               └── presentation/
│                   └── controller/           # REST controller tests
│                       ├── LoginControllerTest.java
│                       ├── UserControllerTest.java
│                       ├── VehicleControllerTest.java
│                       ├── OwnerControllerTest.java
│                       └── AccessEventControllerTest.java
│
├── target/                                   # Compiled artifacts
├── pom.xml                                   # Maven dependencies & plugins
├── Dockerfile                                # Container configuration
├── mvnw & mvnw.cmd                           # Maven wrapper
└── README.md                                 # This file
```

---

## 💾 Database

### Schema Overview

The database uses PostgreSQL 16 with the following main entities:

- **users** - System users with authentication credentials
- **owners** - Company/individual owners
- **vehicles** - Registered vehicles with license plates
- **access_events** - Historical access records

### Database Migrations

Database migrations are managed by **Flyway** and stored in:
```
src/main/resources/db/migration/
```

Naming convention: `V{version}__{description}.sql`

**Flyway automatically**:
- Versioning schema changes
- Tracking applied migrations
- Preventing duplicate migrations
- Supporting rollbacks (with undo scripts)

### Connecting to Database

```bash
# Using psql CLI
psql -h localhost -U admin -d access_control_db -W

# Using connection string
Connection: postgresql://admin:password@localhost:5432/access_control_db
```

### Common Database Commands

```sql
-- List all tables
\dt

-- View table structure
\d table_name

-- Check migration history
SELECT * FROM flyway_schema_history;
```

---

## 🔒 Security

### JWT Authentication

The service implements **JWT (JSON Web Token)** based authentication:

1. **Token Generation**: Issued upon successful login with 1-hour expiration
2. **Token Format**: Bearer token in Authorization header
3. **Token Validation**: RSA signature verification using public key
4. **Stateless**: No session storage required

### Security Headers

```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

### RSA Key Pair

- **Private Key** (`authz.pem`): Used for signing tokens
- **Public Key** (`authz.pub`): Used for token validation

#### Generating New Keys

```bash
# Generate private key (PKCS8 format for Java)
openssl genrsa -out authz.pem 2048
openssl pkcs8 -topk8 -in authz.pem -out authz_private.pem -nocrypt

# Generate public key
openssl rsa -in authz.pem -pubout -out authz.pub
```

### Role-Based Access Control (RBAC)

Custom annotations control access to endpoints:

```java
@CanWriteAccessEvent  // Write permission for access events
@CanReadAccessEvent   // Read permission for access events
@CanManageUsers       // User management permission
@CanManageVehicles    // Vehicle management permission
```

### Security Best Practices Implemented

✅ Password hashing (Spring Security)  
✅ JWT token expiration  
✅ CORS configuration  
✅ HTTPS ready (configure in production)  
✅ Input validation & sanitization  
✅ SQL injection prevention (parameterized queries via JPA)  

---

## 🧪 Testing

### Test Strategy

The project implements a comprehensive testing approach with **20+ test files** covering:

- **Unit Tests**: Domain logic, value objects, and use cases with Mockito
- **Integration Tests**: Full Spring context with MockMvc and Testcontainers
- **Mapper Tests**: MapStruct entity-to-domain conversion validation
- **Controller Tests**: REST endpoint behavior and request/response handling

### Test Coverage

| Category | Files | Coverage |
|----------|-------|----------|
| **Domain Tests** | 4 files | CPF, Name, RG, DocumentFactory |
| **Use Case Tests** | 4 files | Login, Registration, Vehicle Status updates |
| **Mapper Tests** | 6 files | User, Vehicle, Scope, Login, AccessEvent, Owner |
| **Controller Tests** | 5 files | Login, User, Vehicle, Owner, AccessEvent |
| **Integration Tests** | 1 base class | Testcontainers PostgreSQL support |
| **Total** | **20 files** | High coverage of critical paths |

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginControllerTest

# Run specific test method
mvn test -Dtest=LoginControllerTest#shouldLoginSuccessfully

# Run with detailed output
mvn test -X

# Skip tests during build
mvn clean package -DskipTests
```

### Integration Testing with Testcontainers

The project uses **Testcontainers** with PostgreSQL for realistic integration tests:

```java
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AccessControlSystemIntegrationTest {
    
    @Container
    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("test_db")
                    .withUsername("postgres_test")
                    .withPassword("postgres_test");
    
    @DynamicPropertySource
    protected static void configureProperties(DynamicPropertyRegistry registry) {
        // Dynamic property configuration for test database
    }
}
```

**Benefits**:
- ✅ Real PostgreSQL instance during tests
- ✅ Database migrations run automatically
- ✅ Transactional test isolation
- ✅ Consistent test environment across machines

### Test Example - Unit Test (Mockito)

```java
@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {
    
    @InjectMocks
    private LoginUserUseCaseImpl useCase;
    
    @Mock
    private UserGateway userGateway;
    
    @Mock
    private LoginGateway loginGateway;
    
    @Mock
    private ScopeGateway scopeGateway;
    
    @Test
    @DisplayName("Given valid credentials, when logging in, then should return token")
    void shouldLoginSuccessfully() {
        // Test implementation
    }
}
```

### Test Example - Integration Test (MockMvc)

```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LoginControllerTest extends AccessControlSystemIntegrationTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ScopeRepository scopeRepository;
    
    @BeforeEach
    void setup() {
        // Setup test data with real database
    }
    
    @Test
    @DisplayName("When posting valid login request then should return JWT token")
    void shouldLoginEndpointReturnToken() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
```

### Test Reports

Test reports are generated in:
```
target/surefire-reports/
```

Generate and view HTML report:
```bash
# Maven generates surefire report
mvn surefire-report:report

# Open report
open target/site/surefire-report.html
```

### Running Tests in Docker

```bash
# Build image with tests
docker build -f backend-service/Dockerfile .

# Or run tests before Docker build
mvn clean verify

# Run tests with Docker Compose
docker-compose -f docker-compose.yaml run --rm backend-service mvn test
```

### Test Best Practices Implemented

✅ **Descriptive Test Names**: Using `@DisplayName` annotation  
✅ **Arrange-Act-Assert**: Clear test structure  
✅ **Mocking External Dependencies**: Isolated unit tests with Mockito  
✅ **Real Database Testing**: Integration tests with Testcontainers  
✅ **Transactional Isolation**: Tests don't affect each other  
✅ **Test Data Management**: BeforeEach setup and cleanup  
✅ **Assertion Libraries**: JUnit 5 assertions

---

## 🏗️ Build & Deployment

### Building the Application

#### Development Build

```bash
# Build with all tests
mvn clean package

# Build and run immediately
mvn clean package spring-boot:run

# Build with test output
mvn clean package -X
```

#### Production Build

```bash
# Build with optimizations (skip tests)
mvn clean package -DskipTests

# Build specific profile
mvn clean package -Pprod

# Build with detailed logging
mvn -Dorg.slf4j.simpleLogger.defaultLogLevel=debug clean package
```

### Build Output Artifacts

```bash
# Main executable JAR
target/access-control-system-0.0.1-SNAPSHOT.jar

# Original JAR (without Spring Boot repackaging)
target/access-control-system-0.0.1-SNAPSHOT.jar.original

# Test results
target/surefire-reports/
target/test-classes/
```

### Running the Application

#### Development Mode

```bash
# Via Maven
mvn spring-boot:run

# Via compiled JAR
java -jar target/access-control-system-0.0.1-SNAPSHOT.jar

# With custom port
SERVER_PORT=9090 java -jar target/access-control-system-0.0.1-SNAPSHOT.jar

# With increased heap size
java -Xmx1024m -Xms512m -jar target/access-control-system-0.0.1-SNAPSHOT.jar
```

#### Production Mode (Docker)

```bash
# Build Docker image
docker build -f backend-service/Dockerfile \
  -t access-control-system:1.0.0 .

# Run container with all environment variables
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e POSTGRES_HOST=postgres \
  -e POSTGRES_PORT=5432 \
  -e POSTGRES_DB=access_control_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=secure_password \
  -e JWT_PUBLIC_KEY="$(cat authz.pub)" \
  -e JWT_PRIVATE_KEY="$(cat authz.pem)" \
  --name backend-service \
  access-control-system:1.0.0

# View container logs
docker logs -f backend-service

# Stop container
docker stop backend-service
```

#### Docker Compose Deployment

```bash
# Start all services (PostgreSQL + Backend)
docker-compose up -d

# View all running services
docker-compose ps

# View service logs
docker-compose logs -f backend-service

# Stop all services
docker-compose down

# Stop and remove volumes (full cleanup)
docker-compose down -v
```

### Pre-Deployment Checklist

Before deploying to production, verify:

- [ ] All tests pass: `mvn clean verify`
- [ ] No compilation warnings: `mvn clean compile`
- [ ] Application starts without errors
- [ ] Database migrations are valid
- [ ] Environment variables are configured:
  - [ ] POSTGRES_HOST, POSTGRES_PORT, POSTGRES_DB
  - [ ] POSTGRES_USER, POSTGRES_PASSWORD
  - [ ] JWT_PUBLIC_KEY, JWT_PRIVATE_KEY
  - [ ] SPRING_PROFILES_ACTIVE=prod
- [ ] JWT keys are generated and valid
- [ ] Swagger documentation is accessible
- [ ] Health endpoint responds: `GET /actuator/health`
- [ ] Database connection is established
- [ ] All required ports are open (8080, 5432)
- [ ] Logging is configured correctly
- [ ] No hardcoded credentials in code
- [ ] Docker image builds successfully
- [ ] Performance testing completed

### Deployment Environments

#### Local Development
```bash
# PostgreSQL: localhost:5432
# Application: localhost:8080
mvn spring-boot:run
```

#### Staging (Docker Compose)
```bash
# Uses docker-compose.yaml
docker-compose -f docker-compose.yaml up -d
# Verify at localhost:8080
```

#### Production (Kubernetes/Docker)
```bash
# Use production secrets for environment variables
# Use resource limits for containers
# Enable health checks and monitoring
# Configure auto-scaling policies
# Set up CI/CD pipeline for auto-deployment
```

### Release Process

1. **Create Release Branch**: `git checkout -b release/1.0.0`
2. **Update Version**: Update pom.xml version
3. **Build & Test**: `mvn clean verify`
4. **Create Release**: `git tag -a v1.0.0`
5. **Build Artifacts**: `mvn clean package`
6. **Build Docker Image**: `docker build -t access-control-system:1.0.0 .`
7. **Push to Registry**: `docker push your-registry/access-control-system:1.0.0`
8. **Deploy**: Update deployment manifests
9. **Verify**: Run smoke tests
10. **Document**: Update changelog and release notes

---

## 🔧 Environment Variables

### Required Variables

```bash
# Database Configuration
POSTGRES_HOST=localhost              # PostgreSQL host
POSTGRES_PORT=5432                  # PostgreSQL port
POSTGRES_DB=access_control_db       # Database name
POSTGRES_USER=admin                 # Database user
POSTGRES_PASSWORD=securepass123     # Database password

# JWT Configuration
JWT_PUBLIC_KEY=<RSA_PUBLIC_KEY>     # RSA public key (PEM format)
JWT_PRIVATE_KEY=<RSA_PRIVATE_KEY>   # RSA private key (PKCS8 format)
```

### Optional Variables

```bash
# Spring Configuration
SPRING_PROFILES_ACTIVE=dev          # Active profile (dev/prod)
SERVER_PORT=8080                    # Application port
LOGGING_LEVEL_ROOT=INFO             # Root logging level

# JPA Configuration
SPRING_JPA_SHOW_SQL=true            # Show SQL queries
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

# Swagger Configuration
SPRINGDOC_SWAGGER_UI_PATH=/swagger/index.html
```

### Loading Environment Variables

#### Linux/macOS

```bash
export POSTGRES_HOST=localhost
export POSTGRES_DB=access_control_db
# ... set other variables
```

#### Windows (PowerShell)

```powershell
$env:POSTGRES_HOST = "localhost"
$env:POSTGRES_DB = "access_control_db"
# ... set other variables
```

#### Using .env File

```bash
# Create .env file
cat > .env << EOF
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=access_control_db
POSTGRES_USER=admin
POSTGRES_PASSWORD=securepass123
JWT_PUBLIC_KEY=...
JWT_PRIVATE_KEY=...
EOF

# Load with Docker Compose
docker-compose --env-file .env up
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Connection Refused (PostgreSQL)

```
Error: Connection refused
```

**Solution**:
- Verify PostgreSQL is running: `docker-compose ps`
- Check database credentials in environment variables
- Verify database port is open: `telnet localhost 5432`
- Check PostgreSQL logs: `docker-compose logs postgres`

#### 2. JWT Key Format Error

```
Error: Invalid key format
```

**Solution**:
- Ensure keys are in PEM format
- For private key, use PKCS8 format
- Verify keys don't have line breaks: `echo -n "key_content"`
- Regenerate keys if needed

#### 3. Database Migration Failed

```
Error: Flyway migration validation failed
```

**Solution**:
- Check migration files in `src/main/resources/db/migration/`
- Verify SQL syntax
- Check database user has proper permissions
- Reset migrations: `DELETE FROM flyway_schema_history;` (dev only)

#### 4. Port Already in Use

```
Error: Port 8080 is already in use
```

**Solution**:
```bash
# Find process using port 8080
lsof -i :8080
# or on Windows
netstat -ano | findstr :8080

# Kill process or use different port
SERVER_PORT=8081 mvn spring-boot:run
```

#### 5. Out of Memory

```
Error: Java heap space
```

**Solution**:
```bash
# Increase heap size
java -Xmx1024m -Xms512m -jar application.jar

# Or set environment variable
export JAVA_OPTS="-Xmx1024m -Xms512m"
```

### Debug Mode

Enable detailed logging:

```bash
# Via environment variable
LOGGING_LEVEL_ROOT=DEBUG mvn spring-boot:run

# Via application.yaml
logging:
  level:
    root: DEBUG
    com.arthurscarpin.acs: DEBUG
```

### Health Check

```bash
# Check application health
curl http://localhost:8080/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "diskSpace": {"status": "UP"},
    "livenessState": {"status": "UP"},
    "readinessState": {"status": "UP"}
  }
}
```

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Commit** your changes:
   ```bash
   git commit -m "feat: add new feature"
   ```
4. **Push** to the branch:
   ```bash
   git push origin feature/your-feature-name
   ```
5. **Create** a Pull Request

### Coding Standards

- **Java**: Follow Google Java Style Guide
- **Naming**: Use clear, descriptive names
- **Comments**: Document complex logic
- **Tests**: Write tests for new features
- **Commits**: Use conventional commits (`feat:`, `fix:`, `docs:`)

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

Examples:
```
feat(auth): add JWT refresh token support
fix(vehicle): correct license plate validation regex
docs(api): update endpoint documentation
```

### Pull Request Process

1. Update README.md if documentation changed
2. Ensure all tests pass: `mvn clean test`
3. Update CHANGELOG if applicable
4. Get at least 2 approvals
5. Squash commits before merge

### Code Review Checklist

- [ ] Code follows style guidelines
- [ ] Changes are well-commented
- [ ] Tests are added/updated
- [ ] No new warnings generated
- [ ] Documentation is updated
- [ ] Commits are clean and descriptive

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](../../LICENSE) file for details.

### MIT License Summary

✅ **You can**:
- Use commercially
- Modify the code
- Distribute
- Use privately

⚠️ **You must**:
- Include license and copyright notice
- State changes made to the code

❌ **You cannot**:
- Hold the authors liable
- Use trademarks

---

## 📞 Support & Contact

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/arthurscarpin/access-control-system/issues)
- **Discussions**: [GitHub Discussions](https://github.com/arthurscarpin/access-control-system/discussions)
- **Email**: scarpinarthur.dev@gmail.com

### Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT.io - JWT Debugger](https://jwt.io)
- [OpenAPI/Swagger Specification](https://swagger.io/specification/)

### Useful Links

- 📖 [Project Documentation](https://github.com/arthurscarpin/access-control-system/wiki)
- 🐛 [Report a Bug](https://github.com/arthurscarpin/access-control-system/issues/new)
- 💡 [Request a Feature](https://github.com/arthurscarpin/access-control-system/issues/new)
- 📰 [Release Notes](https://github.com/arthurscarpin/access-control-system/releases)

---

## 👨‍💻 Author

**Arthur Carpin**
- Email: scarpinarthur.dev@gmail.com
- GitHub: [@arthurscarpin](https://github.com/arthurscarpin)

---

## 📝 Changelog

### Version 0.0.1-SNAPSHOT (Current)
- Initial release
- User authentication and registration
- Owner management
- Vehicle registration and tracking
- Access event validation and history
- JWT security implementation
- Swagger/OpenAPI documentation

---

<div align="center">

**Made with ❤️ by Arthur Carpin**

[⬆ Back to top](#access-control-system---backend-service)

</div>
