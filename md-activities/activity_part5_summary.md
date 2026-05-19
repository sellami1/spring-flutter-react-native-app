# Activity Part 5 Summary

## What was added
- Created Sprint 5 backlog file in csv-sprints.
- Added Kafka producers in etudiant-service and grading-service for student and note creation events.
- Added notification-service to consume Kafka events and log simulated notifications.
- Added observability stack configuration (ELK, Prometheus, Grafana) plus Logstash pipeline and Prometheus scrape config.
- Added Actuator metrics exposure and Logstash JSON logging for services.
- Added Docker Compose updates for Kafka, notification-service, and observability services.
- Added health checks for etudiant-service, grading-service, and notification-service.

## How to test (commands only, do not run here)

### Set host for remote server access
```bash
export PUBLIC_HOST=homserver.your-domain-or-ip
```

If you run everything directly on the same machine and test from that machine, you can skip this and keep localhost defaults.

### Start the stack
```bash
docker compose up --build
```

### Verify Kafka notifications
```bash
curl -X POST http://${PUBLIC_HOST:-localhost}:8081/api/etudiants \
  -H "Content-Type: application/json" \
  -d '{"cin":"CIN123","nom":"Test User","dateNaissance":"2000-01-01","anneePremiereInscription":2020,"age":24}'

curl -X POST http://${PUBLIC_HOST:-localhost}:8082/api/notes \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"matiere":"Math","valeur":15.5}'

# Then check notification-service logs:
docker logs -f students-notification-service
```

### Check health endpoints
```bash
curl http://${PUBLIC_HOST:-localhost}:8081/actuator/health
curl http://${PUBLIC_HOST:-localhost}:8082/actuator/health
curl http://${PUBLIC_HOST:-localhost}:8083/actuator/health
```

### Check metrics
```bash
curl http://${PUBLIC_HOST:-localhost}:8081/actuator/prometheus
curl http://${PUBLIC_HOST:-localhost}:8082/actuator/prometheus
curl http://${PUBLIC_HOST:-localhost}:8083/actuator/prometheus
```

### Validate observability UIs
- Kibana: http://${PUBLIC_HOST:-localhost}:5601
- Prometheus: http://${PUBLIC_HOST:-localhost}:9090
- Grafana: http://${PUBLIC_HOST:-localhost}:3002
