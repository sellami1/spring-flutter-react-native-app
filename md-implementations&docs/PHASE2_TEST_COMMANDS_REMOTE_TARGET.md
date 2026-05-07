# Phase 2 - Remote Target Variant: Test Execution Commands

## Overview
This variant is for running tests from a local PC against an app deployed on a remote server with Docker Compose.

Source compose reference: [docker-compose.yml](../docker-compose.yml)

## Assumptions
- Remote server is already running the stack with Docker Compose.
- API Gateway is exposed on remote port 8080.
- Frontend is exposed on remote port 3000.
- You can reach the remote host from your local PC.

Set these once on your local PC:

```bash
export REMOTE_HOST=<remote-host-or-ip>
export FRONTEND_URL=http://$REMOTE_HOST:3000
export API_BASE_URL=http://$REMOTE_HOST:8080
```

Optional if HTTPS is enabled:

```bash
export FRONTEND_URL=https://<remote-frontend-domain>
export API_BASE_URL=https://<remote-api-domain>
```

---

## 0. Remote Health Checks

### 0.1 Quick reachability checks from local PC
```bash
curl -I "$FRONTEND_URL"
curl -I "$API_BASE_URL/actuator/health"
```

### 0.2 Validate exposed endpoints used by your stack
```bash
curl -I "$API_BASE_URL/api/etudiants"
curl -I "$API_BASE_URL/api/departements"
```

### 0.3 (Optional) Check remote containers via SSH
```bash
ssh <remote-user>@$REMOTE_HOST 'cd /path/to/project && docker compose ps'
ssh <remote-user>@$REMOTE_HOST 'docker logs --tail=100 students-api-gateway'
```

---

## 1. Setup Dependencies (Local PC only)

### 1.1 Frontend/Cypress dependencies
```bash
cd frontend
npm install
```

### 1.2 Backend/Gatling dependencies (if running Gatling from local source)
```bash
cd rest-spring-api
./mvnw -DskipTests dependency:go-offline
```

---

## 2. Unit/Integration/Coverage Scope Clarification

These commands validate source code and are not "deployed app" checks:
- `./mvnw test`
- `./mvnw verify`
- JaCoCo report/check

Use them in CI or in a source checkout environment. For deployed remote-app validation, use sections 5, 6, and 7 below.

---

## 5. E2E Tests with Cypress Against Remote App (DM-34)

### 5.1 Run Cypress interactively against remote frontend
```bash
cd frontend
CYPRESS_baseUrl="$FRONTEND_URL" npm run cypress:open
```

### 5.2 Run Cypress headless against remote frontend
```bash
cd frontend
CYPRESS_baseUrl="$FRONTEND_URL" npm run cypress:run
```

### 5.3 Run specific E2E file against remote
```bash
cd frontend
CYPRESS_baseUrl="$FRONTEND_URL" npx cypress run --spec "cypress/e2e/etudiants.cy.ts"
```

### 5.4 Run in specific browser
```bash
cd frontend
CYPRESS_baseUrl="$FRONTEND_URL" npx cypress run --browser chrome
CYPRESS_baseUrl="$FRONTEND_URL" npx cypress run --browser firefox
```

Expected outcome:
- E2E tests validate the app exactly as served from the remote dockerized stack.

---

## 6. Stress Tests with Gatling Against Remote API (DM-35)

### 6.1 Run Gatling against remote gateway
```bash
cd rest-spring-api
./mvnw gatling:test \
  -Dgatling.simulationClass=simulations.StudentServiceSimulation \
  -Dgatling.baseUrl="$API_BASE_URL"
```

### 6.2 Run with custom duration/users (if simulation supports parameters)
```bash
cd rest-spring-api
./mvnw gatling:test \
  -Dgatling.simulationClass=simulations.StudentServiceSimulation \
  -Dgatling.baseUrl="$API_BASE_URL" \
  -Dgatling.duration=60
```

### 6.3 View Gatling report generated on local PC
```bash
# Linux example
xdg-open target/gatling/*/index.html
```

Expected outcome:
- Load is generated from local PC to remote app.
- Metrics reflect real network latency and remote runtime behavior.

---

## 7. Complete Remote Validation Pipeline

This pipeline validates the deployed remote app from local PC.

```bash
# 1) Configure target
export REMOTE_HOST=<remote-host-or-ip>
export FRONTEND_URL=http://$REMOTE_HOST:3000
export API_BASE_URL=http://$REMOTE_HOST:8080

# 2) Health checks
curl -I "$FRONTEND_URL"
curl -I "$API_BASE_URL/actuator/health"

# 3) E2E on remote app
cd frontend
CYPRESS_baseUrl="$FRONTEND_URL" npm run cypress:run

# 4) Stress on remote app
cd ../rest-spring-api
./mvnw gatling:test \
  -Dgatling.simulationClass=simulations.StudentServiceSimulation \
  -Dgatling.baseUrl="$API_BASE_URL"
```

Optional combined command:
```bash
export REMOTE_HOST=<remote-host-or-ip> && \
export FRONTEND_URL=http://$REMOTE_HOST:3000 && \
export API_BASE_URL=http://$REMOTE_HOST:8080 && \
curl -I "$FRONTEND_URL" && \
curl -I "$API_BASE_URL/actuator/health" && \
cd frontend && CYPRESS_baseUrl="$FRONTEND_URL" npm run cypress:run && \
cd ../rest-spring-api && ./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation -Dgatling.baseUrl="$API_BASE_URL"
```

---

## 8. Continuous Integration for Remote Target

### Option A: Keep current CI for source quality + add remote smoke stage
- Existing CI continues to run `./mvnw clean verify` and local/in-CI checks.
- Add a post-deploy job that executes remote smoke and E2E against deployed URLs.

Example remote smoke step:
```yaml
- name: Remote smoke checks
  run: |
    curl -f https://<frontend-domain>
    curl -f https://<api-domain>/actuator/health
```

Example remote E2E step:
```yaml
- name: Cypress against deployed environment
  run: |
    cd frontend
    CYPRESS_baseUrl=https://<frontend-domain> npm run cypress:run
```

### Option B: Run tests directly on remote host via SSH from local PC
```bash
ssh <remote-user>@$REMOTE_HOST 'cd /path/to/project && docker compose ps'
ssh <remote-user>@$REMOTE_HOST 'cd /path/to/project/rest-spring-api && ./mvnw test'
```

Use Option B for operational checks, not as a replacement for CI reproducibility.

---

## 9. Remote Troubleshooting

### Cypress cannot reach remote URL
```bash
curl -v "$FRONTEND_URL"
```
- Check firewall/security group for port 3000.
- Check reverse proxy and TLS configuration.

### Gatling receives 4xx/5xx from remote gateway
```bash
curl -v "$API_BASE_URL/actuator/health"
curl -v "$API_BASE_URL/api/etudiants"
```
- Verify API Gateway routes and service discovery state.
- Check remote logs for `students-api-gateway` and `students-etudiant-service`.

### Remote app works in browser but tests fail intermittently
- Add startup wait/retries before E2E execution.
- Ensure test data isolation and cleanup.

---

## 10. Report Locations (Remote Target Variant)

| Test Type | Report Location |
|-----------|-----------------|
| Cypress run artifacts | `frontend/cypress/screenshots/` and `frontend/cypress/videos/` (local PC) |
| Gatling report | `rest-spring-api/target/gatling/*/index.html` (local PC) |
| JaCoCo report | `rest-spring-api/target/site/jacoco/index.html` (only when source tests are run) |

---

## 11. Quick Reference (Remote)

```bash
# Set remote target
export REMOTE_HOST=<remote-host-or-ip>
export FRONTEND_URL=http://$REMOTE_HOST:3000
export API_BASE_URL=http://$REMOTE_HOST:8080

# E2E on remote app
cd frontend && CYPRESS_baseUrl="$FRONTEND_URL" npm run cypress:run

# Stress on remote app
cd rest-spring-api && ./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation -Dgatling.baseUrl="$API_BASE_URL"

# Health checks
curl -I "$FRONTEND_URL"
curl -I "$API_BASE_URL/actuator/health"
```
