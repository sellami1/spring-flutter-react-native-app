# grading-service

CRUD service for student grades (notes). Provides Swagger/OpenAPI and registers with Eureka.

## Endpoints
- `GET /api/notes`
- `GET /api/notes/{id}`
- `POST /api/notes`
- `PUT /api/notes/{id}`
- `DELETE /api/notes/{id}`

## Runtime env vars
- `SERVER_PORT` (default: `8082`)
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` (default: `http://localhost:8761/eureka`)
- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `5433`)
- `DB_NAME` (default: `students_db`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: `postgres`)
