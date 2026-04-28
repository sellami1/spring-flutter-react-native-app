# Implementation Plan: Dockerize `rest-spring-api` and Push to Docker Hub

## Scope
- Project in scope only: `rest-spring-api/`
- Deliverable: containerized Spring Boot REST API + bundled `static/index.html` UI served by the same app
- Registry target: Docker Hub
- Constraint respected in this phase: planning only (no commands executed, no code changes)

## Assumptions
- `rest-spring-api` is a Maven Spring Boot application producing an executable JAR.
- UI files under `src/main/resources/static/` are included in the built JAR.
- Default server port remains `8080` unless overridden at runtime.
- Docker Hub repository exists or will be created before push.

## High-Level Strategy
1. Build the Spring Boot JAR with Maven.
2. Package the JAR into a lightweight runtime image via multi-stage Docker build.
3. Add `.dockerignore` to reduce build context and speed up builds.
4. Validate container locally (API + UI availability).
5. Tag image with semantic and immutable tags.
6. Authenticate and push tags to Docker Hub.
7. Capture run/pull commands for reproducibility.

## Planned Artifacts (inside `rest-spring-api/`)
- `Dockerfile`
- `.dockerignore`
- (Optional) `README` section update for container usage

## Detailed Steps

### 1) Verify Packaging Inputs
- Confirm `pom.xml` builds a runnable Spring Boot artifact.
- Confirm `src/main/resources/static/index.html` is packaged into JAR resources.
- Confirm required runtime properties are externalizable through environment variables.

### 2) Create Multi-Stage Dockerfile
- Stage A (`build`): use Maven + JDK image to build JAR.
- Stage B (`runtime`): use JRE/JDK slim runtime image and copy only the built JAR.
- Expose `8080`.
- Define entrypoint using `java -jar`.
- Prefer deterministic paths and explicit artifact naming to avoid wildcard ambiguity.

Suggested structure (to implement later):
- Base build image: `maven:3.9-eclipse-temurin-17`
- Runtime image: `eclipse-temurin:17-jre`
- Build command: `mvn -DskipTests clean package`
- Runtime command: `java -jar app.jar`

### 3) Add `.dockerignore`
Exclude unnecessary files from build context:
- `target/`
- `.git/`
- `.idea/`, `.vscode/`
- `*.iml`
- local logs/temp files

### 4) Local Image Build and Runtime Validation
Planned validation checklist:
- Image builds successfully.
- Container starts and listens on port `8080`.
- API endpoint responds (example: `/api/students` or project-defined endpoint).
- UI is reachable at `/` and successfully calls backend endpoints.
- No dependency on host-only URLs (e.g., hardcoded `localhost` in browser JS when running from same origin).

### 5) Docker Hub Tagging Convention
Use at least two tags:
- Version tag (example: `v1.0.0`)
- Mutable channel tag (example: `latest`)

Image naming template:
- `<dockerhub-username>/rest-spring-api:<tag>`

### 6) Publish to Docker Hub
Planned push flow:
1. Authenticate: `docker login`
2. Tag local image with both tags.
3. Push each tag.
4. Verify image visibility and pull instructions on Docker Hub.

### 7) Smoke-Test Pulled Image
- Pull from Docker Hub on a clean environment.
- Run container with `-p 8080:8080`.
- Re-validate `/` (UI) and API endpoints.

## Command Plan (to execute later, not now)
From `rest-spring-api/`:

```bash
# Build application JAR
./mvnw -DskipTests clean package

# Build image
docker build -t <dockerhub-username>/rest-spring-api:v1.0.0 .

# Additional tag
docker tag <dockerhub-username>/rest-spring-api:v1.0.0 <dockerhub-username>/rest-spring-api:latest

# Local run check
docker run --rm -p 8080:8080 <dockerhub-username>/rest-spring-api:v1.0.0

# Docker Hub push
docker login
docker push <dockerhub-username>/rest-spring-api:v1.0.0
docker push <dockerhub-username>/rest-spring-api:latest
```

## Risks and Mitigations
- Hardcoded UI API base URL risk:
  - Mitigation: ensure same-origin relative API path usage in UI.
- Large image size risk:
  - Mitigation: multi-stage build + slim runtime image.
- Platform mismatch risk (arm64/amd64):
  - Mitigation: optionally publish multi-arch image via `buildx` in a later enhancement.

## Definition of Done
- `rest-spring-api` contains `Dockerfile` and `.dockerignore`.
- Image builds locally and runs successfully.
- UI (`/`) and API endpoints both work from the containerized app.
- Docker Hub repository has pushed `vX.Y.Z` and `latest` tags.
- Pull/run instructions are documented.

## Out of Scope
- Any changes in sibling projects (`mobile_flutter/`, `mobile-react-native/`, root-level unrelated files).
- Kubernetes manifests or deployment automation.
- CI/CD pipeline creation.
