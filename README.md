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

---

## Code Review Process & Conventions

### Overview
All pull requests must go through a mandatory review process before merging into `main` or `version-3` branches. This ensures code quality, knowledge sharing, and consistency across the codebase.

### Review Requirements
- **Minimum Approvals:** At least 1 approval required before merge
- **Review Deadline:** Reviews should be completed within **48 hours** of PR creation
- **Mandatory Checks:** All CI/CD checks and tests must pass
- **Conversation Resolution:** All review comments must be resolved before merge

### Review Workflow

1. **Create a Branch**
   ```bash
   git checkout version-3
   git checkout -b feature/your-feature-name
   ```

2. **Create a Pull Request**
   - Push your changes and create a PR targeting `version-3`
   - Fill out the PR template completely
   - Link the related Jira ticket
   - Request reviewers

3. **Address Feedback**
   - Reviewers will add comments and suggestions
   - Address all **blocking comments** before merge
   - Push follow-up commits to the same branch
   - Reply to comments explaining changes

4. **Approval & Merge**
   - Once approved and all checks pass, merge the PR
   - Delete the feature branch after merging
   - Update the related Jira ticket to "Done"

### Reviewer Responsibilities

- **Understand the Context:** Read the PR description and related Jira ticket
- **Review Code Quality:** Check for readability, maintainability, and adherence to project standards
- **Verify Tests:** Ensure tests cover the changes appropriately
- **Check for Regressions:** Consider potential side effects or breaking changes
- **Be Constructive:** Provide helpful, specific feedback with suggestions for improvement
- **Respect Timeline:** Aim to complete reviews within 48 hours

### Best Practices

#### For Authors
- Keep PRs focused and reasonably sized (aim for <400 lines of code)
- Provide clear, descriptive commit messages
- Include relevant unit and integration tests
- Update documentation if needed
- Run tests locally before pushing

#### For Reviewers
- Approve only when you're confident in the changes
- Use "Request Changes" for blocking issues
- Use "Comment" for suggestions or discussions
- Ask questions if you don't understand the rationale
- Acknowledge good code with positive feedback

### Labels
- `bug` — Bug fix
- `enhancement` — New feature or improvement
- `documentation` — Documentation update
- `devops` — DevOps or infrastructure changes
- `testing` — Test-related changes
- `WIP` — Work in progress (do not merge)
- `blocked` — Blocked by another PR or issue

### Escalation
If a review is not completed within 48 hours:
1. Send a reminder to the reviewer
2. Contact the team lead if still unresolved after 24 more hours
3. In urgent cases, escalate to the project manager
