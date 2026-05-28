# Frontend Service

Angular frontend for the Access Control System. The application provides the browser UI for authentication, dashboard metrics, captures, users, owners, vehicles, scopes, and access events. In production Docker builds, Angular is compiled into static files and served by Nginx.

## Highlights

- Angular 21 standalone application.
- JWT-based authenticated API calls to `backend-service`.
- Feature areas for login, dashboard, captures, users, owners, vehicles, scopes, and access events.
- Shared layout components for header, sidebar, menu, and notifications.
- Production Docker image built with Node 20 and served with Nginx.
- Root-level Docker Compose integration exposed on `localhost:4200`.

## Technology Stack

| Area | Technology |
| --- | --- |
| Runtime | Angular 21 |
| Language | TypeScript 5.9 |
| Styling | Tailwind CSS 4, CSS |
| Icons | `@lucide/angular` |
| Authentication client | JWT token storage and HTTP interceptor |
| Testing | Angular unit test builder, Vitest, jsdom |
| Production serving | Nginx |
| Packaging | Docker multi-stage build |

## Project Layout

```text
frontend-service
+-- public
+-- src
|   +-- app
|   |   +-- core          # DTOs, models, guards, interceptors, API services
|   |   +-- features      # Login, dashboard, captures, owners, users, vehicles, scopes, access events
|   |   +-- shared        # Header, sidebar, menu, notification components
|   +-- environments      # Local and production API configuration
+-- angular.json
+-- package.json
+-- Dockerfile
+-- nginx.conf
```

## Configuration

The application reads its API base URL from Angular environment files:

| File | Purpose | API URL |
| --- | --- | --- |
| `src/environments/environment.ts` | Local development with `ng serve` | `http://localhost:8080` |
| `src/environments/environment.prod.ts` | Production Docker build | `http://backend-service:8080` |

The production Docker image is intended to run with the root `docker-compose.yaml`, where the backend service name is `backend-service`.

## Running Locally

### Prerequisites

- Node.js 20+
- npm
- Backend API running on `http://localhost:8080`

### Install Dependencies

```bash
npm install
```

### Development Server

```bash
npm start
```

The application runs on:

```text
http://localhost:4200
```

The Angular dev server reloads automatically when source files change.

## Building

Create a production build:

```bash
npm run build -- --configuration production
```

Angular writes the static browser bundle to:

```text
dist/frontend-service/browser
```

## Testing

Run unit tests:

```bash
npm test
```

## Docker

Build the frontend image from this directory:

```bash
docker build -t frontend-service .
```

Run the Nginx container directly:

```bash
docker run --rm -p 4200:80 frontend-service
```

The image uses a multi-stage build:

1. `node:20-alpine` installs dependencies with `npm ci` and runs the Angular production build.
2. `nginx:alpine` serves `dist/frontend-service/browser` from `/usr/share/nginx/html`.
3. `nginx.conf` enables SPA routing by falling back to `index.html`.

## Docker Compose

From the repository root, start the frontend with the rest of the platform:

```bash
docker compose up --build frontend-service
```

For the full environment:

```bash
docker compose up --build
```

The Compose service uses:

| Setting | Value |
| --- | --- |
| Build context | `./frontend-service` |
| Dockerfile | `Dockerfile` |
| Container name | `frontend-service` |
| Port mapping | `4200:80` |
| Dependency | `backend-service` |

After startup, open:

```text
http://localhost:4200
```

## Operational Notes

- The container serves static files only; API requests are made by the browser to the configured API URL.
- Nginx is configured with `try_files $uri $uri/ /index.html` so browser refreshes on Angular routes work.
- `.dockerignore` excludes `node_modules`, `dist`, and Angular cache from the Docker build context.
- Current production builds may emit Angular warning `NG8113` for an unused `LucideBell` import in `SharedMenu`; this does not fail the build.
