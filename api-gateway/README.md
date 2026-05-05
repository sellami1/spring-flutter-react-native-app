# api-gateway

Spring Cloud Gateway used as the single entry point for all API calls.

## Routes
- `/api/etudiants/**`  -> `lb://etudiant-service`
- `/api/departements/**` -> `lb://etudiant-service`
- `/api/notes/**` -> `lb://grading-service`

## Runtime env vars
- `SERVER_PORT` (default: `8080`)
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` (default: `http://localhost:8761/eureka`)
