# Implementation Plan - Activity Part 4 (DM Sprint 4)

## Scope
This plan covers all tasks from Part 4:
- Q1: Git branch and Sprint setup
- Q2-Q3: Full testing strategy (unit, integration, E2E, stress) + Testcontainers + coverage gate
- Q4: GitHub-Jira integration
- Q5: Xray test management and CI publication
- Q6: New auth microservice (Express, MongoDB, JWT)

## Sprint Backlog Mapping
Backlog source: [sprint4.csv](sprint4.csv)
- DM-30 to DM-31: planning and Jira preparation
- DM-32 to DM-37: quality engineering implementation
- DM-38 to DM-40: traceability and test governance
- DM-41 to DM-43: authentication microservice and deployment integration
- DM-44: documentation and closure

## Phase 1 - Planning and Branching (Q1)
### Tasks
1. Create `version-4` from `version-3` (already done in terminal history).
2. Create Jira sprint named `DM Sprint 4`.
3. Create stories/tasks matching DM-30..DM-44.
4. Define acceptance criteria per ticket before development.

### Deliverables
- Branch `version-4`
- Jira sprint board populated with DM Sprint 4 backlog

### Acceptance Criteria
- All Part 4 requirements are represented as Jira issues.
- Each issue has summary, label, assignee, and sprint assignment.

## Phase 2 - Test Strategy and Quality Gate (Q2 + Q3)

### 2.1 Unit Tests (DM-32)
### Implementation
- Add/extend tests in service layer (EtudiantService and related business logic).
- Use JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`).
- Cover happy path + failure/edge cases.

### Coverage Targets
- Methods: create, list, update, delete, filter by year, age logic.
- Assertions must verify behavior, not just line execution.

### 2.2 Integration Tests with Testcontainers (DM-33, DM-37)
### Implementation
- Add `org.testcontainers:junit-jupiter` and `org.testcontainers:postgresql` (test scope).
- Create integration tests with `@SpringBootTest` and `@Testcontainers`.
- Inject dynamic DB properties through `@DynamicPropertySource`.

### Expected Results
- Tests run against real PostgreSQL container.
- No dependency on local manually managed test database.

### 2.3 E2E Tests with Cypress (DM-34)
### Implementation
- In [frontend](frontend), add Cypress setup and test folder `cypress/e2e/`.
- Write at least:
  - list rendering test for `/etudiants`
  - create student flow for `/etudiants/nouveau`
- Add stable selectors (`data-testid`) where needed.

### 2.4 Stress Tests with Gatling (DM-35)
### Implementation
- Add Gatling Maven plugin and simulation class under `src/gatling/java/simulations/`.
- Scenario: concurrent users hitting `GET /api/etudiants`.
- Capture mean response time, p95, success rate.

### 2.5 JaCoCo Gate >= 80% (DM-36)
### Implementation
- Configure `jacoco-maven-plugin` in [rest-spring-api/pom.xml](rest-spring-api/pom.xml) with:
  - `prepare-agent`
  - `check` goal and line coverage minimum `0.80`
- Bind to `verify` lifecycle.

### Validation Commands
```bash
cd rest-spring-api
./mvnw clean verify
```

### Acceptance Criteria
- Build fails automatically below 80% line coverage.
- Unit + integration tests run in CI.

## Phase 3 - Jira Traceability Integration (Q4)

### Tasks (DM-38)
1. Install/connect GitHub for Jira app.
2. Ensure repository is linked to Jira project.
3. Enforce naming conventions:
   - Branch: `feature/DM-XX-short-description`
   - Commit: `DM-XX: meaningful change description`
   - PR title includes issue key.

### Acceptance Criteria
- Jira ticket shows linked branch, commits, and PRs.
- Team follows convention in all Sprint 4 changes.

## Phase 4 - Xray Test Governance (Q5)

### Scope
This phase turns automated tests into traceable Jira/Xray assets so Sprint 4 evidence is visible outside the repository.

### Assumptions / Decisions
1. Jira project uses Xray Cloud unless the team already operates Xray Data Center.
2. Automated test results are published from CI, not manually from a laptop.
3. Existing JUnit/Surefire XML becomes the primary source for unit and integration evidence.
4. Cypress and Gatling reports are attached as supplementary evidence when supported by the Xray import flow.

### Tasks (DM-39, DM-40)
1. Install and configure Xray for the Jira project.
2. Define the Xray issue model for Sprint 4.
3. Map automated suites to Xray Test issues.
4. Create a Sprint 4 Test Plan and Test Execution lifecycle.
5. Add a CI publishing step that uploads results after `mvn verify`.
6. Document the traceability convention so stories, tests, and executions stay aligned.

### 4.1 Xray project setup
#### Deliverables
- Xray enabled in the Jira project used for Sprint 4.
- Service account or API token configured for CI publishing.
- A documented naming convention for Test, Test Plan, and Test Execution issues.

#### Configuration Notes
- Prefer a dedicated CI identity instead of a personal Jira token.
- Store secrets in GitHub Actions secrets only.
- Restrict the CI identity to upload test evidence and create executions, not administer the project.

#### Acceptance Criteria
- A pipeline token can create or update a Test Execution in the Jira project.
- Team members can view Xray issues without extra manual setup.

### 4.2 Xray issue model
#### Deliverables
- Xray Test issues for the most important automated checks.
- One Sprint 4 Test Plan covering the whole backlog slice.
- One Test Execution per CI run or per meaningful pipeline execution.

#### Recommended mapping
- Unit tests: one Test issue per service-level behavior cluster.
- Integration tests: one Test issue per container-backed scenario or endpoint group.
- E2E tests: one Test issue per user journey in the frontend.
- Stress tests: one Test issue for the Gatling scenario and a separate execution record for each load run.

#### Naming convention
- Test: `DM-XX - short functional description`
- Test Plan: `Sprint 4 - DM coverage plan`
- Test Execution: `CI - <branch> - <yyyy-mm-dd> - <run-id>`

#### Acceptance Criteria
- Every critical automated suite has a corresponding Xray Test issue.
- Test issues are linked back to the originating story or technical task.

### 4.3 Traceability rules
#### Deliverables
- Story-to-test links in Jira.
- Test Plan coverage summary for Sprint 4.
- Execution status that shows passed, failed, or blocked evidence.

#### Link strategy
- Link each DM story to the Test issues that validate it.
- Use coverage links so Jira can answer: which story is covered by which tests, and which tests are currently automated.
- Keep one owner per Test issue to avoid duplicate or conflicting mappings.

#### Acceptance Criteria
- A Jira story shows at least one linked Test issue when it has automated coverage.
- The Test Plan displays which stories are covered and which remain manual or unimplemented.

### 4.4 CI publishing flow
#### Workflow sequence
1. Build and test: `mvn verify`.
2. Collect test result files from Surefire/Failsafe and any additional generated reports.
3. Publish the execution to Xray using the CI secret.
4. Fail the upload step if the API rejects the payload or authentication fails.

#### Required CI inputs
- `XRAY_CLIENT_ID` and `XRAY_CLIENT_SECRET`, or the project-specific auth mechanism used by the chosen Xray deployment.
- Jira project key or Test Plan key.
- Paths to test result files produced by Maven.

#### Output expectations
- A new Xray Test Execution appears in Jira for the run.
- The execution contains the imported test cases and their pass/fail counts.
- The pipeline logs the execution key or URL for quick navigation.

#### Acceptance Criteria
- A green CI run results in a visible Xray execution with imported results.
- A failing CI run still uploads evidence so the failure is traceable in Jira.

### 4.5 Report sources and evidence handling
#### Deliverables
- Surefire XML as the canonical unit/integration input.
- Optional attachments or links for Cypress and Gatling artifacts.
- A short markdown note describing where to find each artifact in CI.

#### Evidence rules
- Use deterministic report paths so the upload job does not depend on local shell state.
- Keep raw reports in CI artifacts even when they are imported into Xray.
- Prefer structured results over screenshots for automated evidence.

#### Acceptance Criteria
- The team can re-open a CI run and find both the Xray execution and the raw build artifacts.

### 4.6 Failure handling and fallback behavior
#### Failure cases to handle
- Missing or invalid Xray secret.
- Jira API rate limit or temporary outage.
- Result file not generated because the test phase failed early.
- Partial execution upload when a subset of reports is present.

#### Expected behavior
- The pipeline should mark the upload step as failed when authentication or API submission fails.
- The build should keep the original test failure as the primary failure signal.
- If upload is temporarily unavailable, preserve local artifacts so a re-run can publish them later.

#### Acceptance Criteria
- Xray upload failures are visible and do not silently pass.
- The test run outcome remains readable even if Jira is unreachable.

### 4.7 Validation checklist
#### Manual checks
- Confirm the Jira project contains the expected Test, Test Plan, and Test Execution issues.
- Confirm one Sprint 4 story shows linked test coverage.
- Confirm CI logs include the Xray execution key or URL.

#### Command flow
```bash
cd rest-spring-api
./mvnw verify
```
Then run the CI upload step with the same artifacts and secrets used by GitHub Actions.

#### Acceptance Criteria
- The workflow can be replayed locally or in CI without changing the report format.
- Jira/Xray evidence is produced for every Sprint 4 test run.

### GitHub Actions integration
- Workflow step sequence:
  1. `mvn verify`
  2. Xray upload with token from `XRAY_TOKEN` secret

### Acceptance Criteria
- Each CI run publishes test execution to Xray.
- Jira shows traceability from Story -> Test -> Execution status.
- The published execution matches the same run that produced the Maven test reports.

## Phase 5 - Authentication Microservice (Q6)

### 5.1 Service Scaffold (DM-41)
### Implementation
- Create `auth-service/` Node.js project.
- Add dependencies:
  - `express`, `mongoose`, `bcrypt`, `jsonwebtoken`, `dotenv`, `cors`
- Suggested structure:
  - `src/app.js`
  - `src/server.js`
  - `src/config/db.js`
  - `src/models/User.js`
  - `src/routes/auth.js`
  - `src/middleware/errorHandler.js`

### 5.2 Authentication Endpoints (DM-42)
### Implementation
- `POST /auth/register`
  - validate payload
  - reject duplicate usernames
  - hash password with bcrypt
- `POST /auth/login`
  - validate credentials
  - return signed JWT (expiry 1h)

### Security Requirements
- JWT secret from environment variable only.
- Never store plain passwords.
- Return generic invalid credentials message.

### 5.3 Docker Compose Integration (DM-43)
### Implementation
- Add `auth-service` and `mongodb` services to [docker-compose.yml](docker-compose.yml).
- Add env vars (`MONGO_URI`, `JWT_SECRET`, `PORT`).
- Configure container networking and startup dependency.

### Acceptance Criteria
- `docker compose up` starts auth-service + mongodb successfully.
- Register/login endpoints functional through mapped port.

## Phase 6 - Documentation and Closure (DM-44)

### Tasks
1. Document local run instructions and required environment variables.
2. Document test strategy and expected evidence (coverage report, Cypress run, Gatling report, Xray execution).
3. Add troubleshooting notes for Docker/Testcontainers/JWT configuration.

### Definition of Done (Sprint 4)
- All DM Sprint 4 tickets are completed and linked in Jira.
- Coverage >= 80% enforced in CI.
- Unit + integration + E2E + stress tests available and executable.
- GitHub-Jira and Xray integrations operational.
- Auth service deployed via Docker Compose with MongoDB and JWT flows working.

## Recommended Execution Order
1. DM-30, DM-31
2. DM-32, DM-33, DM-36, DM-37
3. DM-34, DM-35
4. DM-38, DM-39, DM-40
5. DM-41, DM-42, DM-43
6. DM-44

## Risks and Mitigations
- Docker unavailable during tests: verify Docker daemon before Testcontainers jobs.
- Flaky E2E tests: rely on stable selectors and deterministic seed data.
- Coverage inflation without quality assertions: enforce review checklist on test assertions.
- JWT secret misconfiguration: fail fast on startup if secret is missing.
