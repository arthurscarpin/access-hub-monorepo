# Frontend Service

Angular operations console for the Access Control System. The application provides the browser UI used to authenticate operators, manage platform data, upload capture ZIP files, monitor capture processing, and inspect access history.

In production, Angular is compiled to static files and served by Nginx.

## Responsibilities

- Login and token storage with optional "remember me" behavior.
- Route protection through an Angular guard.
- Authenticated REST calls through an HTTP interceptor.
- Dashboard cards for vehicles, owners, and access-event totals.
- Owner, vehicle, user, and scope management screens.
- Capture ZIP upload with `IN` / `OUT` direction selection.
- Realtime capture notifications over STOMP WebSocket.
- Access-event history table with result filtering and pagination.

## Architecture

```text
src/app
+-- core
|   +-- dto             # request/response DTOs used by API services
|   +-- guards          # auth guard
|   +-- interceptors    # bearer-token HTTP interceptor
|   +-- models          # frontend domain models
|   +-- services        # REST and WebSocket services
+-- features
|   +-- access-events   # access history page and table component
|   +-- captures        # upload page and ZIP dropzone
|   +-- dashboard       # dashboard page, metric cards, recent events
|   +-- login           # login page, login card, forgot-password modal
|   +-- owners          # owner list and registration modal
|   +-- scopes          # scope card grid
|   +-- users           # user list and registration modal
|   +-- vehicles        # vehicle list, registration modal, status toggle
+-- shared
|   +-- components      # header, menu, sidebar, notification dropdown
+-- testing            # shared unit-test fixtures
```

The app uses Angular standalone components. Feature folders own their page components and smaller presentational/workflow components. Cross-cutting API access, guards, interceptors, models, and DTOs live in `core`.

## Routes

| Route | Component | Guarded |
| --- | --- | --- |
| `/login` | `LoginPage` | no |
| `/dashboard` | `DashboardPage` | yes |
| `/owners` | `OwnersPage` | yes |
| `/users` | `UsersPage` | yes |
| `/vehicles` | `VehiclesPage` | yes |
| `/scopes` | `ScopesPage` | yes |
| `/captures` | `CapturesPage` | yes |
| `/access-events` | `AccessEventsPage` | yes |

The root path redirects to `/login`.

## Feature Overview

### Authentication

- `AuthService` calls `POST /login`.
- Tokens are stored in `localStorage` when "remember me" is selected, otherwise in `sessionStorage`.
- `authGuard` validates token expiration before allowing protected routes.
- `authInterceptor` attaches `Authorization: Bearer <token>` to protected API calls.
- Logout clears storage and redirects to `/login`.

### Dashboard

- Loads vehicle, owner, and access-event totals.
- Shows recent access events.
- Uses computed Angular signals for card configuration.

### Captures

- Accepts only `.zip` files.
- Sends multipart form data to `POST /captures/upload` with fields:
  - `file`
  - `direction`
- Starts a STOMP subscription to `/topic/capture/{captureId}` when the backend returns a capture id.
- Shows success/error states and realtime processing notifications.

### Management

- Owners: list, pagination, document formatting, registration modal.
- Vehicles: list, pagination, registration modal, status toggle.
- Users: list, scope display, registration modal with scope selection.
- Scopes: mapped scope cards grouped by resource/action semantics.

### Notifications

- `WebsocketService` connects to `environment.webSocketUrl`.
- Notifications are stored as Angular signals.
- The shared menu displays unread count.
- The dropdown can mark individual notifications, mark all as read, clear all, and expand AI reasoning.

## Technology Stack

| Area | Technology |
| --- | --- |
| Framework | Angular 21 |
| Language | TypeScript 5.9 |
| State | Angular signals and computed signals |
| Forms | Angular Reactive Forms |
| HTTP | Angular `HttpClient` with functional interceptor |
| Routing | Angular Router |
| Realtime | `@stomp/stompjs` |
| Icons | `@lucide/angular` |
| Styling | Tailwind CSS 4 and global CSS |
| Tests | Angular unit-test builder, Vitest, jsdom |
| Coverage | `@vitest/coverage-v8` |
| Production serving | Nginx |
| Packaging | Docker multi-stage build |

## Configuration

Environment files:

| File | API URL | WebSocket URL |
| --- | --- | --- |
| `src/environments/environment.ts` | `http://localhost:8080` | `ws://localhost:8080/ws` |
| `src/environments/environment.prod.ts` | `http://backend-service:8080` | `ws://backend-service:8080/ws` |

The production Docker image is intended to run inside the root `docker-compose.yaml`, where the backend hostname is `backend-service`.

## Running Locally

Prerequisites:

- Node.js 20+
- npm
- Backend API running on `http://localhost:8080`

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm start
```

Open:

```text
http://localhost:4200
```

## Build

Production build:

```bash
npm run build -- --configuration production
```

Output:

```text
dist/frontend-service/browser
```

## Tests

Run tests once:

```bash
npm test -- --watch=false
```

Run tests in watch mode:

```bash
npm test
```

Run tests with coverage and LCOV output:

```bash
npm test -- --watch=false --coverage --coverage-reporters=lcov --coverage-reporters=text-summary
```

Coverage output used by CI/Sonar:

```text
coverage/frontend-service/lcov.info
```

Current suite coverage validates services, guard, interceptor, pages, components, forms, signals, pagination, uploads, STOMP notifications, and rendering behavior.

## Docker

Build:

```bash
docker build -t frontend-service .
```

Run:

```bash
docker run --rm -p 4200:80 frontend-service
```

Dockerfile stages:

1. `node:20-alpine` installs dependencies with `npm ci` and builds Angular.
2. `nginx:alpine` serves `dist/frontend-service/browser`.
3. `nginx.conf` falls back to `index.html` for client-side routes.

## Docker Compose

From the repository root:

```bash
docker compose up --build frontend-service
```

Full platform:

```bash
docker compose up --build
```

Compose settings:

| Setting | Value |
| --- | --- |
| Build context | `./frontend-service` |
| Container name | `frontend-service` |
| Port mapping | `4200:80` |
| Dependency | `backend-service` |

## CI/CD

Workflow:

```text
.github/workflows/frontend-service-ci-cd.yaml
```

Jobs:

- `build-test`: installs dependencies, runs Angular/Vitest coverage, builds production bundle, uploads `dist` and coverage artifacts.
- `sonar`: downloads coverage and runs SonarCloud with `coverage/frontend-service/lcov.info`.
- `security`: runs CodeQL for JavaScript/TypeScript and builds the project.

## Operational Notes

- The Nginx container serves static files only. Browser requests still go to the configured backend URL.
- `@stomp/stompjs` may emit an Angular CommonJS optimization warning during production build; the build still succeeds.
- `.dockerignore` excludes `node_modules`, `dist`, `.angular`, Git metadata, and local logs from the Docker build context.
