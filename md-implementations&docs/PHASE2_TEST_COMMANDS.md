# Phase 2 - Test Strategy & Quality Gate: Test Execution Commands

## Overview
This document provides all test commands required for Phase 2 of Sprint 4, organized by testing level.

## Prerequisites
Before running tests, ensure:
1. Docker daemon is running (for Testcontainers integration tests)
2. PostgreSQL is not running on local port 5432 (Testcontainers needs to use that port)
3. Maven 3.8+ is installed
4. Node.js 16+ and npm installed
5. All dependencies downloaded

## 1. Setup Dependencies

### 1.1 Java/Maven Dependencies
Update Maven dependencies for rest-spring-api:
```bash
cd rest-spring-api
./mvnw clean install -DskipTests
```

### 1.2 Frontend/Node Dependencies
Update Node dependencies for frontend:
```bash
cd frontend
npm install
# OR if using yarn:
yarn install
```

---

## 2. Unit Tests (DM-32)

### Run All Unit Tests
```bash
cd rest-spring-api
./mvnw test
```

### Run Specific Unit Test Class
```bash
cd rest-spring-api
./mvnw test -Dtest=StudentServiceTest
```

### Run Unit Tests with Coverage Report
```bash
cd rest-spring-api
./mvnw test jacoco:report
# Report location: target/site/jacoco/index.html
```

### Expected Output
- ✓ All tests should pass
- ✓ 30+ test cases executed
- ✓ Coverage report generated at `target/site/jacoco/index.html`

---

## 3. Integration Tests with Testcontainers (DM-33)

### Run All Integration Tests (Testcontainers included)
```bash
cd rest-spring-api
./mvnw verify
```
*Note: This also runs unit tests + JaCoCo coverage check*

### Run Only Integration Tests
```bash
cd rest-spring-api
./mvnw test -Dtest=*IntegrationTest
```

### Run Integration Tests with Detailed Output
```bash
cd rest-spring-api
./mvnw test -Dtest=StudentServiceIntegrationTest -X
```

### Docker Verification (ensure Docker is running)
Before running, verify Docker is available:
```bash
docker --version
docker ps
```

### Expected Output
- ✓ PostgreSQL container starts automatically
- ✓ 15+ integration test cases pass
- ✓ Database operations verified
- ✓ PostgreSQL container stops after tests complete

---

## 4. JaCoCo Code Coverage Gate (DM-36)

### Run Maven Verify with Coverage Check (80% minimum)
```bash
cd rest-spring-api
./mvnw clean verify
```

### View Coverage Report
After running verify, open the report:
```bash
# macOS/Linux
open target/site/jacoco/index.html

# Windows
start target/site/jacoco/index.html

# Or open manually in browser: file://<full-path>/target/site/jacoco/index.html
```

### Expected Output
- ✓ Coverage >= 80% required
- ✓ Build fails if below 80%
- ✓ Summary shows: COVEREDRATIO threshold: 0.80

### Coverage Goals by Package
| Package | Target |
|---------|--------|
| service | >= 85% |
| controller | >= 80% |
| entity | >= 75% |
| dto | >= 70% |
| mapper | >= 90% |

---

## 5. E2E Tests with Cypress (DM-34)

### Prerequisites - Start Full Stack Locally
```bash
# Start all services with docker-compose
cd .. # go to root
docker compose up

# OR start individually (in separate terminals):

# Terminal 1: Start PostgreSQL + Redis
docker run --name postgres-test -e POSTGRES_PASSWORD=spring -p 5432:5432 -d postgres:15
docker run --name redis-test -p 6379:6379 -d redis

# Terminal 2: Start Rest API
cd rest-spring-api
./mvnw spring-boot:run

# Terminal 3: Start Frontend
cd frontend
npm run dev
# Frontend will be at http://localhost:3000
```

### 5.1 Open Cypress Test Runner (Interactive)
```bash
cd frontend
npm run cypress:open
# This opens the Cypress GUI where you can:
# - Select a browser (Chrome, Firefox, Edge)
# - Click on test files to run
# - See real-time results
```

### 5.2 Run Cypress Tests Headless (CI Mode)
```bash
cd frontend
npm run cypress:run
```

### 5.3 Run Specific Cypress Test File
```bash
cd frontend
npx cypress run --spec "cypress/e2e/etudiants.cy.ts"
```

### 5.4 Run Tests in Specific Browser
```bash
cd frontend
npx cypress run --browser chrome
npx cypress run --browser firefox
npx cypress run --browser edge
```

### Expected Output
- ✓ 30+ test cases pass
- ✓ Coverage includes:
  - Student list display
  - Create student flow
  - Filtering & search
  - Error handling
  - Responsive design
  - Accessibility checks

---

## 6. Stress Tests with Gatling (DM-35)

### Prerequisites - Start API Server
```bash
cd rest-spring-api
./mvnw spring-boot:run
# API should be at http://localhost:8080
```

### 6.1 Run Gatling Simulation
```bash
cd rest-spring-api
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation
```

### 6.2 Run Gatling with Custom Parameters
```bash
cd rest-spring-api
./mvnw gatling:test \
  -Dgatling.simulationClass=simulations.StudentServiceSimulation \
  -Dgatling.baseUrl=http://localhost:8080 \
  -Dgatling.duration=60
```

### 6.3 View Gatling Report
After simulation completes:
```bash
# Report is generated at: rest-spring-api/target/gatling/

# Open the latest report
# On macOS:
open target/gatling/*/index.html

# On Windows:
start target/gatling/*/index.html

# On Linux:
firefox target/gatling/*/index.html
```

### Expected Output
- ✓ 50+ concurrent users ramped up
- ✓ Response times < 5000ms (max)
- ✓ 95th percentile < 2000ms
- ✓ 100% success rate
- ✓ Report shows throughput, response times, errors

---

## 7. Complete Test Pipeline (All Together)

### Run Full Test Suite (Recommended for CI/CD)
```bash
cd rest-spring-api

# Clean, build, run all tests, and check coverage
./mvnw clean verify

# Then run Gatling
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation
```

### Full E2E Testing (from project root)
```bash
# 1. Start all services
docker compose up -d

# 2. Wait for services to be ready (30-45 seconds)
sleep 60

# 3. Run backend tests
cd rest-spring-api
./mvnw clean verify

# 4. Run stress tests
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation

# 5. Run frontend E2E tests
cd ../frontend
npm run cypress:run

# 6. Generate reports summary
echo "=== Test Reports Ready ==="
echo "Coverage: ../rest-spring-api/target/site/jacoco/index.html"
echo "Gatling: ../rest-spring-api/target/gatling/*/index.html"
echo "Cypress: ../frontend/cypress/videos/"
```

---

## 8. Continuous Integration (GitHub Actions)

### Example GitHub Actions Workflow
```yaml
name: Test Pipeline

on: [push, pull_request]

jobs:
  backend-tests:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: spring
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Run tests
        run: |
          cd rest-spring-api
          ./mvnw clean verify
      - name: Upload coverage reports
        uses: codecov/codecov-action@v3
        with:
          files: ./rest-spring-api/target/site/jacoco/index.html

  frontend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: |
          cd frontend
          npm install
      - name: Run Cypress tests
        run: |
          cd frontend
          npm run cypress:run
      - name: Upload Cypress videos
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: cypress-videos
          path: frontend/cypress/videos/
```

---

## 9. Troubleshooting

### Unit Tests Fail with "Database connection error"
**Solution:** These are unit tests and should NOT connect to DB. If failing:
```bash
cd rest-spring-api
./mvnw test -Dtest=StudentServiceTest -X
# Check that @ExtendWith(MockitoExtension.class) is used, not @SpringBootTest
```

### Integration Tests Fail with "Cannot connect to Docker daemon"
**Solution:** Ensure Docker is running
```bash
# Start Docker daemon
docker --version
docker ps

# If fails, start Docker Desktop or service
```

### Cypress Tests Fail with "Cannot find module"
**Solution:** Reinstall dependencies
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run cypress:run
```

### Gatling Simulation Not Found
**Solution:** Verify simulation class path
```bash
cd rest-spring-api
ls -la src/gatling/java/simulations/
# Should show: StudentServiceSimulation.java

# Run with correct class name
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation
```

### Coverage Below 80%
**Solution:** Add more tests or ensure existing tests have assertions
```bash
cd rest-spring-api

# Check coverage report
mvn jacoco:report
open target/site/jacoco/index.html

# Focus on classes with lowest coverage
# Add unit or integration tests for those classes
```

---

## 10. Test Report Locations

| Test Type | Report Location |
|-----------|-----------------|
| Unit Tests | `rest-spring-api/target/surefire-reports/` |
| Integration Tests | `rest-spring-api/target/surefire-reports/` |
| Coverage (JaCoCo) | `rest-spring-api/target/site/jacoco/index.html` |
| Gatling | `rest-spring-api/target/gatling/*/index.html` |
| Cypress Screenshots | `frontend/cypress/screenshots/` |
| Cypress Videos | `frontend/cypress/videos/` |

---

## 11. Success Criteria (Definition of Done)

- ✓ All unit tests pass (30+)
- ✓ All integration tests pass (15+)
- ✓ Code coverage >= 80%
- ✓ All E2E tests pass (30+)
- ✓ Gatling stress test passes (< 5s max response time)
- ✓ 100% test success rate
- ✓ No flaky tests (retry count = 0)

---

## 12. Quick Reference Commands

```bash
# Unit + Integration + Coverage
cd rest-spring-api && ./mvnw clean verify

# E2E Tests
cd frontend && npm run cypress:run

# Stress Tests
cd rest-spring-api && ./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation

# View Coverage Report
open rest-spring-api/target/site/jacoco/index.html

# View Gatling Report
open rest-spring-api/target/gatling/*/index.html

# Run all in sequence
cd rest-spring-api && ./mvnw clean verify && \
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation && \
cd ../frontend && npm run cypress:run
```
