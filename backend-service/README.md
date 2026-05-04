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
- 🧪 **Comprehensive Testing** - Unit tests with Spring Test Framework
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
    key: ${JWT_PUBLIC_KEY}
  private:
    key: ${JWT_PRIVATE_KEY}
```

### Active Profiles

Run with specific profiles:
```bash
# Development (local database, debug logging)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
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

The project is organized following Clean Architecture principles:

```
src/main/java/com/arthurscarpin/acs/
├── core/                          # Enterprise Business Rules
│   ├── accessevent/              # Access event domain logic
│   ├── owner/                    # Owner domain logic
│   ├── user/                     # User domain logic
│   ├── vehicle/                  # Vehicle domain logic
│   └── pagination/               # Pagination utilities
│
└── infrastructure/               # External frameworks & DB
    ├── configuration/            # Spring configuration classes
    ├── gateway/                  # External service integrations
    ├── mapper/                   # DTO/Domain mappers (MapStruct)
    ├── persistence/              # JPA repositories & entities
    └── presentation/             # REST controllers & DTOs
        ├── controller/           # REST endpoints
        ├── request/              # Request DTOs
        ├── response/             # Response DTOs
        ├── advice/               # Global exception handling
        └── documentation/        # OpenAPI documentation
```

### Key Design Patterns

- **Use Case Pattern**: Business logic encapsulated in reusable use case classes
- **Repository Pattern**: Data access abstraction with JPA repositories
- **Mapper Pattern**: Separation of domain models from DTOs (MapStruct)
- **Dependency Injection**: Spring IoC container for loose coupling
- **Exception Handling**: Global exception handler with custom exceptions

### Data Flow

```
Request → Controller → Mapper → Use Case → Domain Logic → Repository → Database
Response ← Mapper ← Use Case ← Domain Logic ← Repository ← Database
```

---

## 📁 Project Structure

```
backend-service/
├── src/
│   ├── main/
│   │   ├── java/com/arthurscarpin/acs/
│   │   │   ├── core/
│   │   │   │   ├── accessevent/
│   │   │   │   │   ├── domain/
│   │   │   │   │   ├── usecase/
│   │   │   │   │   └── gateway/
│   │   │   │   ├── owner/
│   │   │   │   ├── user/
│   │   │   │   ├── vehicle/
│   │   │   │   └── pagination/
│   │   │   └── infrastructure/
│   │   │       ├── configuration/
│   │   │       ├── gateway/
│   │   │       ├── mapper/
│   │   │       ├── persistence/
│   │   │       └── presentation/
│   │   │
│   │   └── resources/
│   │       ├── application.yaml         # Main configuration
│   │       ├── db/migration/            # Flyway SQL migrations
│   │       ├── authz.pem & authz.pub    # JWT key pair
│   │       └── logback-spring.xml       # Logging configuration
│   │
│   └── test/
│       └── java/com/arthurscarpin/acs/
│           └── core/
│               ├── accessevent/
│               ├── owner/
│               ├── user/
│               └── vehicle/
│
├── target/                              # Compiled artifacts
├── pom.xml                              # Maven dependencies
├── Dockerfile                           # Container configuration
├── mvnw & mvnw.cmd                      # Maven wrapper
└── README.md                            # This file
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

### Test Coverage

The project includes comprehensive unit tests:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AccessEventTest

# Run with coverage report
mvn test jacoco:report
```

### Test Files Location

```
src/test/java/com/arthurscarpin/acs/core/
├── accessevent/
│   ├── domain/AccessEventTest.java
│   └── usecase/GetAccessHistoryUseCaseImplTest.java
├── owner/
│   └── domain/CPFTest.java
├── user/
│   └── usecase/LoginUserUseCaseImplTest.java
└── vehicle/
    ├── domain/PlateTest.java
    └── usecase/RegisterVehicleUseCaseImplTest.java
```

### Running Tests in Docker

```bash
# Build with tests
docker build -f backend-service/Dockerfile --target testing .

# Or run tests before Docker build
mvn clean verify
```

### Test Reports

Test reports are generated in:
```
target/surefire-reports/
```

View HTML report:
```bash
open target/surefire-reports/index.html
```

---

## 🏗️ Build & Deployment

### Building the Application

#### Maven Build

```bash
# Clean and package
mvn clean package

# Skip tests (faster)
mvn clean package -DskipTests

# Build specific profile
mvn clean package -Pprod
```

#### Output Artifacts

```
target/access-control-system-0.0.1-SNAPSHOT.jar          # Executable JAR
target/access-control-system-0.0.1-SNAPSHOT.jar.original # Original JAR
```

### Running the Application

#### Development

```bash
mvn spring-boot:run
```

#### Production (JAR)

```bash
java -jar target/access-control-system-0.0.1-SNAPSHOT.jar
```

#### Production (Docker)

```bash
# Build image
docker build -f backend-service/Dockerfile \
  -t access-control-system:1.0.0 .

# Run container
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
```

### Deployment Checklist

- [ ] Database is configured and running
- [ ] Environment variables are set
- [ ] JWT keys are generated and configured
- [ ] Application builds successfully
- [ ] All tests pass
- [ ] Database migrations are applied
- [ ] Swagger docs are accessible
- [ ] API endpoints respond correctly
- [ ] Logs are being captured properly

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
