Here is the extracted content from the PDF, formatted with code fences for each language or command type as requested.

```text
ACTIVITÉS D'INTÉGRATION DE COMPÉTENCES — PARTIE 2

Objectif général :
Enrichir le projet Spring Boot de la Partie 1 en ajoutant de la logique métier testée, une interface web légère, une image Docker publiée, un déploiement Kubernetes, et une architecture de micro service plus complète avec cache, gestion des erreurs, documentation et traçabilité Jira.
```

## Q1 — Créer une branche Git version-2

```bash
git checkout -b version-2
```

## Q2 — Ajouter une méthode age() à l'entité Etudiant

```java
public int age() {
    return Period.between(this.dateNaissance, LocalDate.now()).getYears();
}
```

## Q3 — Tester la méthode age() avec la syntaxe Gherkin (BDD)

```gherkin
Feature: Calcul de l'âge d'un étudiant

  Scenario: Étudiant né il y a 22 ans
    Given un étudiant avec la date de naissance "2002-04-07"
    When on calcule son âge
    Then l'âge retourné doit être 23
```

## Q4 — Ajouter une page index.html avec un appel Fetch JavaScript

```html
<!-- Exemple minimal -->
<script>
  fetch('/api/etudiants')
    .then(res => res.json())
    .then(data => {
      // Construire et insérer les éléments dans le DOM
    });
</script>
```

## Q5 — Créer et publier l'image Docker du micro service sur Docker Hub

```bash
docker build -t <votre-username>/etudiant-service:1.0
docker push <votre-username>/etudiant-service:1.0
```

## Q6 — Créer les manifests Kubernetes pour déployer l'application sur K3S

```yaml
# etudiant-deployment.yaml (exemple)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: etudiant-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: etudiant
  template:
    metadata:
      labels:
        app: etudiant
    spec:
      containers:
      - name: etudiant
        image: <votre-username>/etudiant-service:1.0
        ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: etudiant-service
spec:
  selector:
    app: etudiant
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  type: NodePort
```

```yaml
# postgres-deployment.yaml (exemple)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:13
        env:
        - name: POSTGRES_USER
          value: "user"
        - name: POSTGRES_PASSWORD
          value: "password"
        - name: POSTGRES_DB
          value: "etudiantdb"
        ports:
        - containerPort: 5432
---
apiVersion: v1
kind: Service
metadata:
  name: postgres
spec:
  selector:
    app: postgres
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
```

## Q9 — Ajouter une requête personnalisée (firstInscriptionYear) dans le repository

```java
List<Etudiant> findByAnneePremiereInscription(int annee);
```

## Q10 — Ajouter les opérations CRUD complètes pour Etudiant et Département

```http
GET    /api/etudiants
GET    /api/etudiants/{id}
POST   /api/etudiants
PUT    /api/etudiants/{id}
DELETE /api/etudiants/{id}

GET    /api/departements
GET    /api/departements/{id}
POST   /api/departements
PUT    /api/departements/{id}
DELETE /api/departements/{id}
```

## Q11 — Ajouter la gestion des erreurs HTTP standard

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex) {
        // Implementation
    }
}
```

## Q12 — Ajouter la documentation Swagger / OpenAPI

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.x.x</version>
</dependency>
```

## Q13 — Ajouter Redis pour activer le cache

```java
@Cacheable(value = "etudiants")
public List<EtudiantDTO> findAll() { ... }

@CacheEvict(value = "etudiants", allEntries = true)
public EtudiantDTO save(EtudiantDTO dto) { ... }
```

```yaml
# Extrait docker-compose.yml
redis:
  image: redis:alpine
  ports:
    - "6379:6379"
```

## Q14 — Créer un projet Jira Scrum avec deux sprints

```text
Epic : Gestion des Étudiants
  Sprint 1 – API REST de base (Partie 1)
    US-01 : Lister les étudiants
    US-02 : Dockeriser l'application
    ...
  Sprint 2 – Enrichissement (Partie 2)
    US-03 : Méthode age() + tests BDD
    US-04 : Cache Redis
    ...
```

## Structure du dépôt GitHub attendue

```text
/projet-etudiants/
  api-spring-boot/
    src/
      main/
        java/com/example/etudiants/
          controller/
          service/
          repository/
          entity/
          dto/
          mapper/
          config/
        resources/
          static/
            index.html
      test/
        resources/
          features/
            etudiant.feature
    Dockerfile
    pom.xml
  k8s/
    etudiant-deployment.yaml
    postgres-deployment.yaml
  docker-compose.yml
```

## Rappel commit Jira

```bash
git commit -m "PROJ-12 : ajout méthode age()"
```