# Activity Part 3 — Microservices (READACT3)

This file summarizes what has been implemented for [activity_part3.md](activity_part3.md), what remains, and how to push changes using the GitHub workflow (branch + PR templates + branch protection/CI).

## 1) What’s implemented (backend)

### Q1 — Branching (process)
- Expected branch flow: `version-2` → create `version-3` → feature branches → PRs into `version-3`.
- If you haven’t already created `version-3`, do it before pushing any Sprint 3 work.

### Q2 — GitHub workflow (process)
Already present in this repo:
- Issue templates: [.github/ISSUE_TEMPLATE/bug_report.md](.github/ISSUE_TEMPLATE/bug_report.md), [.github/ISSUE_TEMPLATE/feature_request.md](.github/ISSUE_TEMPLATE/feature_request.md)
- PR template: [.github/pull_request_template.md](.github/pull_request_template.md)
- README section: code review process + conventions (see [README.md](README.md))

Note: there is currently no GitHub Actions workflow under `.github/workflows/` in this repository. If you add one later, it will typically run on PRs and must be green before merging.

### Q3 — `grading-service` (CRUD Notes)
Implemented:
- Spring Boot microservice `grading-service` with:
  - Entity `Note(id, studentId, matiere, valeur)`
  - CRUD endpoints under `/api/notes`
  - Validation + standard HTTP errors
  - Swagger/OpenAPI via springdoc

### Q4 — Eureka + Feign + API Gateway
Implemented:
- `eureka-server` (registry) on port `8761`
- `rest-spring-api` updated to run as `etudiant-service` (Eureka client) on port `8081`
- `api-gateway` on port `8080` with routes:
  - `/api/etudiants/**` → `lb://etudiant-service`
  - `/api/departements/**` → `lb://etudiant-service`
  - `/api/notes/**` → `lb://grading-service`
- `grading-service` uses OpenFeign to call `etudiant-service` and verify student existence before creating/updating a note.

### Q7 — docker-compose orchestration
Updated [docker-compose.yml](docker-compose.yml) to run:
- `postgres` (mapped locally as `5433:5432`)
- `redis`
- `eureka-server`
- `etudiant-service`
- `grading-service`
- `api-gateway`

## 2) What remains (per activity requirements)

- Q5: Update mobile app to call the gateway and filter students by department.
- Q6: Create Next.js frontend in `frontend/` (not implemented yet).
- Q7 (optional completeness): add `frontend` service to Compose once Q6 exists.

## 3) How to run and verify (local)

### Option A — Docker Compose (recommended)
From repo root:

```bash
docker compose up --build
```

Check:
- Eureka UI: `http://localhost:8761` (you should see `ETUDIANT-SERVICE`, `GRADING-SERVICE`, and `API-GATEWAY` registered)
- Gateway routing:

```bash
curl -s http://localhost:8080/api/departements | head
curl -s http://localhost:8080/api/etudiants | head
curl -s http://localhost:8080/api/notes | head
```

Feign validation check (expected behavior):
- Creating/updating a note with a non-existing `studentId` should return a `404` with a clear error payload.

### Option B — Run services with Maven (dev)
Run each service in its own terminal, ensuring `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka` and correct DB/Redis env vars.

## 4) Steps to push changes (with the GitHub workflow)

These steps assume branch protection is enabled on `main` and `version-3` (no direct pushes).

1) Start from the right base branch

```bash
git checkout version-3
git pull
```

If `version-3` doesn’t exist yet:

```bash
git checkout version-2
git pull
git checkout -b version-3
```

2) Create a feature branch for your Sprint 3 task

```bash
git checkout -b feature/sprint3-microservices
```

3) Commit your changes

```bash
git status
git add -A
git commit -m "Sprint 3: microservices (eureka/gateway/grading)"
```

4) Push your branch

```bash
git push -u origin feature/sprint3-microservices
```

5) Open a Pull Request into `version-3`
- Base: `version-3`
- Compare: `feature/sprint3-microservices`
- Fill the PR template (Jira ticket + test/verification steps)

6) Wait for checks (if a CI workflow exists)
- If you later add GitHub Actions under `.github/workflows/`, those checks must be green.

7) Get 1 approval and merge
- Resolve all review comments
- Merge (or squash merge) into `version-3`
- Delete the feature branch after merge

8) (Optional) Tag a release / update Jira
- Move the Jira ticket(s) to “Done” and link the PR.

## 5) References

- Activity statement: [activity_part3.md](activity_part3.md)
- Microservices implementation plan(s):
  - [IMPLEMENTATION_PLAN_PART3_MICROSERVICES.md](IMPLEMENTATION_PLAN_PART3_MICROSERVICES.md)
  - [IMPLEMENTATION_PLAN_PART3_Q3_Q4_Q7.md](IMPLEMENTATION_PLAN_PART3_Q3_Q4_Q7.md)
