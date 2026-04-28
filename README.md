# Spring REST API Mobile Students App

Spring Boot backend for students/departments with a built-in static web UI (`index.html`), Docker packaging, and Kubernetes manifests for local K3s deployment.

## Demo First (K3s)

If you want to present the project quickly, start here.

Video link (replace with your final recording):

[Click me for full demo running k3s services](https://drive.google.com/file/d/1OYEPlQyrvSZatXnsc95cg-wzkSSoUy3g/view?usp=sharing)

### Demo Script (recommended order)

1. Show Docker Hub image tags: `v1.0.0` and `latest`.
2. Show Kubernetes files in `k8s/`.
3. Apply manifests on K3s VM.
4. Show pods/services running in namespace `students`.
5. Open UI from `http://<VM_IP>:<NODE_PORT>/`.
6. Show API from `http://<VM_IP>:<NODE_PORT>/api/etudiants`.
7. Perform one CRUD action in UI and refresh API result.

### Demo Commands (run on K3s VM)

```bash
sudo k3s kubectl apply -f k8s/namespace.yaml
sudo k3s kubectl apply -f k8s/postgres-secret.yaml
sudo k3s kubectl apply -f k8s/postgres-pvc.yaml
sudo k3s kubectl apply -f k8s/postgres-deployment.yaml
sudo k3s kubectl apply -f k8s/redis-deployment.yaml
sudo k3s kubectl apply -f k8s/rest-spring-api-deployment.yaml
sudo k3s kubectl -n students get pods,svc
sudo k3s kubectl -n students get svc rest-spring-api
```

## Repository Structure

- `rest-spring-api/`: Spring Boot service + static UI + Dockerfile
- `k8s/`: Kubernetes manifests (namespace, postgres, redis, app)
- `activity_part1.md`, `activity_part2.md`: assignment statements
- `K3S_Q6_DEPLOYMENT_GUIDE.md`: additional deployment notes

## Features Implemented

- Full CRUD API for students and departments
- Business logic for student age
- BDD test feature (Gherkin/Cucumber)
- Static web admin UI served by Spring Boot at `/`
- Global exception handling
- OpenAPI/Swagger integration
- Redis cache integration
- Docker image build and publish flow
- K3s manifests for API + PostgreSQL + Redis

## Tech Stack

- Java 21
- Spring Boot
- Maven Wrapper (`./mvnw`)
- PostgreSQL
- Redis
- Docker
- K3s / Kubernetes

## Docker Image

- Image: `kadhemsellami/rest-spring-api:v1.0.0`
- Runtime port: `8080`

Build and push from `rest-spring-api/`:

```bash
docker build -t kadhemsellami/rest-spring-api:v1.0.0 .
docker tag kadhemsellami/rest-spring-api:v1.0.0 kadhemsellami/rest-spring-api:latest
docker login
docker push kadhemsellami/rest-spring-api:v1.0.0
docker push kadhemsellami/rest-spring-api:latest
```

## Kubernetes Files (Q6)

Required manifests in `k8s/`:

- `namespace.yaml`
- `postgres-secret.yaml`
- `postgres-pvc.yaml`
- `postgres-deployment.yaml`
- `redis-deployment.yaml`
- `rest-spring-api-deployment.yaml`

## Deploy on Local K3s VM

Run from the project root on the K3s machine:

```bash
sudo k3s kubectl apply -f k8s/namespace.yaml
sudo k3s kubectl apply -f k8s/postgres-secret.yaml
sudo k3s kubectl apply -f k8s/postgres-pvc.yaml
sudo k3s kubectl apply -f k8s/postgres-deployment.yaml
sudo k3s kubectl apply -f k8s/redis-deployment.yaml
sudo k3s kubectl apply -f k8s/rest-spring-api-deployment.yaml
```

Verify:

```bash
sudo k3s kubectl -n students get pods,svc
sudo k3s kubectl -n students get svc rest-spring-api
```

Then open:

- `http://<VM_IP>:<NODE_PORT>/`
- `http://<VM_IP>:<NODE_PORT>/api/etudiants`

## Runtime Environment Variables

Configured in the app:

- `SERVER_PORT` (default `8080`)
- `DB_HOST` (default `localhost`)
- `DB_PORT` (default `5433`)
- `DB_NAME` (default `students_db`)
- `DB_USERNAME` (default `postgres`)
- `DB_PASSWORD` (default `postgres`)
- `REDIS_HOST` (default `localhost`)
- `REDIS_PORT` (default `6379`)

In Kubernetes, these are provided through deployment env values and `postgres-secret`.

## Troubleshooting

- Image pull issue: confirm Docker Hub image/tag and public visibility.
- API pod crash: check env values and app logs.
- DB connectivity: verify `postgres` service and secret values.
- NodePort inaccessible: check VM network mode and firewall/NAT rules.

## Notes

- Build/push does not have to be executed on the K3s machine.
- K3s machine must be able to pull the published image from Docker Hub.
