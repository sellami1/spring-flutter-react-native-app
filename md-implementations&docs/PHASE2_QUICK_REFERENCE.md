# Phase 2 Quick Reference - Test Commands Card

## 🚀 Quick Commands

### All Backend Tests (Recommended First Step)
```bash
cd rest-spring-api
./mvnw clean verify
```
✓ Runs: Unit tests, Integration tests, Coverage check  
✓ Expected: All pass, Coverage >= 80%  
✓ Time: ~2-3 minutes

---

### Unit Tests Only
```bash
cd rest-spring-api
./mvnw test
```
✓ 16 test cases  
✓ No DB needed  
✓ Time: ~30 seconds

---

### Integration Tests + Coverage
```bash
cd rest-spring-api
./mvnw clean verify
```
✓ 15 integration test cases  
✓ Real PostgreSQL (auto-started)  
✓ Coverage report generated  
✓ Time: ~2 minutes

---

### View Coverage Report
```bash
open rest-spring-api/target/site/jacoco/index.html
```
or open in browser: `file:///<path>/rest-spring-api/target/site/jacoco/index.html`

---

### Frontend E2E Tests
```bash
cd frontend
npm run cypress:run
```
✓ 30+ test cases  
✓ Requires backend running (docker compose up)  
✓ Time: ~3 minutes

---

### Interactive Cypress Testing
```bash
cd frontend
npm run cypress:open
```
Opens GUI - select browser and tests to run manually

---

### Stress Tests
```bash
cd rest-spring-api
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation
```
✓ 3 load scenarios  
✓ 50-100 concurrent users  
✓ Report at: `target/gatling/*/index.html`  
✓ Time: ~2 minutes

---

## 📋 Test Coverage

| Layer | Tests | File | Command |
|-------|-------|------|---------|
| Unit | 16 | `StudentServiceTest.java` | `mvn test` |
| Integration | 15 | `StudentServiceIntegrationTest.java` | `mvn verify` |
| E2E | 30+ | `etudiants.cy.ts` | `npm run cypress:run` |
| Stress | 3 scenarios | `StudentServiceSimulation.java` | `mvn gatling:test` |
| Coverage | >= 80% | JaCoCo in pom.xml | `mvn verify` |

---

## ⚡ Prerequisites

- Docker running (for Testcontainers)
- Maven 3.8+
- Java 21
- Node 16+
- npm installed

```bash
# Verify prerequisites
docker --version
java -version
mvn -version
node -version
npm -version
```

---

## 🔧 Full Stack Testing

```bash
# Terminal 1: Start all services
cd .. # project root
docker compose up

# Terminal 2: Run backend tests (after ~30s for services to start)
cd rest-spring-api
./mvnw clean verify
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation

# Terminal 3: Run frontend tests
cd frontend
npm run cypress:run
```

---

## 📊 Expected Results

### Unit Tests
- ✓ 16/16 passing
- Time: ~30s
- No external dependencies

### Integration Tests
- ✓ 15/15 passing
- Time: ~90s
- PostgreSQL container auto-managed
- Coverage >= 80%

### E2E Tests
- ✓ 30+/30+ passing
- Time: ~3 minutes
- All scenarios passing
- Responsive design verified

### Stress Tests
- ✓ 100% success rate
- ✓ Max response time < 5s
- ✓ P95 response time < 2s
- ✓ Throughput measured

---

## 📁 Test Locations

```
rest-spring-api/
├── src/
│   ├── test/java/tn/.../service/
│   │   ├── StudentServiceTest.java (16 unit tests)
│   │   └── StudentServiceIntegrationTest.java (15 integration tests)
│   └── gatling/java/simulations/
│       └── StudentServiceSimulation.java (stress tests)
└── target/
    ├── site/jacoco/ (coverage report)
    └── gatling/ (stress test report)

frontend/
├── cypress/
│   ├── e2e/
│   │   └── etudiants.cy.ts (30+ E2E tests)
│   └── support/
│       └── e2e.ts
├── cypress.config.ts
└── package.json
```

---

## 🐛 Troubleshooting Quick Fixes

**Docker not running:**
```bash
docker ps
# If fails, start Docker daemon
```

**Tests timeout:**
```bash
# Increase timeout (in pom.xml or test code)
# Default: 10s, try: 30s
```

**Cypress tests fail:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run cypress:run
```

**Coverage below 80%:**
```bash
# View which files need more tests
open rest-spring-api/target/site/jacoco/index.html
# Add tests to low-coverage classes
```

**Gatling not found:**
```bash
cd rest-spring-api
./mvnw gatling:test -Dgatling.simulationClass=simulations.StudentServiceSimulation
# Ensure API is running on http://localhost:8080
```

---

## 📚 Documentation Files

- **[PHASE2_TEST_COMMANDS.md](PHASE2_TEST_COMMANDS.md)** - Complete reference with all commands
- **[PHASE2_IMPLEMENTATION_SUMMARY.md](PHASE2_IMPLEMENTATION_SUMMARY.md)** - Detailed implementation overview
- **[IMPLEMENTATION_PLAN_PART4.md](IMPLEMENTATION_PLAN_PART4.md)** - Full Sprint 4 plan

---

## ✅ Success Criteria

- [ ] Unit tests: 16/16 passing
- [ ] Integration tests: 15/15 passing
- [ ] Coverage: >= 80%
- [ ] E2E tests: 30+/30+ passing
- [ ] Stress tests: 100% success, < 5s max response
- [ ] All reports generated
- [ ] No flaky tests

---

## 🎯 Typical Execution Flow

```
1. Check prerequisites       (2 min)
   └─ docker ps, java -v, mvn -v

2. Run backend tests         (2-3 min)
   └─ cd rest-spring-api && ./mvnw clean verify

3. Check coverage            (1 min)
   └─ open target/site/jacoco/index.html

4. Start full stack          (30 sec)
   └─ docker compose up (or start services)

5. Run stress tests          (2 min)
   └─ ./mvnw gatling:test

6. Run E2E tests             (3 min)
   └─ cd frontend && npm run cypress:run

Total Time: ~10-15 minutes
```

---

## 📞 Support

For detailed help, see: [PHASE2_TEST_COMMANDS.md](PHASE2_TEST_COMMANDS.md)

For implementation details, see: [PHASE2_IMPLEMENTATION_SUMMARY.md](PHASE2_IMPLEMENTATION_SUMMARY.md)

---

**Phase 2 Status:** ✅ READY FOR EXECUTION
