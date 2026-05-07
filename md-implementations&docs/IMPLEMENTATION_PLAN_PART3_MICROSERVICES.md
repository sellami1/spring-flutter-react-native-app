# Implementation Plan — Partie 3 (Microservices: Eureka + Feign + Gateway + Grading + Next.js + Mobile)

## Scope
Ce plan couvre les activités de `activity_part3.md` (Q1 → Q7) pour faire évoluer le projet vers une architecture microservices complète, orchestrée via `docker-compose.yml`.

## État actuel du dépôt (constaté)
- Backend existant: `rest-spring-api/` expose déjà:
  - `GET /api/departements`
  - `GET /api/etudiants` et `GET /api/etudiants/{id}`
- Déjà présents:
  - `docker-compose.yml` (postgres + redis + rest-spring-api)
  - `.github/` + un plan Q2: `Q2_GITHUB_WORKFLOW_IMPLEMENTATION_PLAN.md`

## Décisions / Hypothèses (à valider avant de coder)
1. **Nom du microservice étudiant**: on réutilise `rest-spring-api/` comme **etudiant-service** (sans forcément renommer le dossier), en configurant:
   - `spring.application.name=etudiant-service`
   - port `8081`
2. **Compatibilité Spring Cloud**: le backend actuel utilise Spring Boot `4.0.5`.
   - Risque: Spring Cloud Netflix (Eureka/Feign/Gateway) n’est pas toujours compatible avec les versions majeures les plus récentes de Spring Boot.
   - Décision recommandée: aligner les nouveaux microservices (et éventuellement `rest-spring-api/`) sur une version Boot/Spring Cloud compatible (souvent Boot `3.2.x/3.3.x` + Spring Cloud 2023.x/2024.x selon compatibilité).
3. **Base de données**: on garde **un seul PostgreSQL** dans docker-compose pour le dev local.
   - Option A (simple): une seule DB `students_db` avec tables étudiants + notes.
   - Option B (plus microservices): 2 DBs (ex: `students_db` + `grading_db`) via scripts d’init.

---

## Q1 — Branch `version-3` + Sprint 3 Jira
### Objectif
Isoler le travail et organiser les US/Tasks.

### Étapes
- Git:
  - `git checkout version-2`
  - `git checkout -b version-3`
- Jira (Sprint 3): créer des User Stories et Tasks correspondant à Q2→Q7.

### Critères d’acceptation
- La branche `version-3` existe et sert de base à toutes les PR de Sprint 3.
- Le Sprint 3 contient une décomposition claire (US + tasks techniques + tests + doc).

---

## Q2 — Workflow GitHub (protection + templates + conventions)
### Objectif
Standardiser la contribution et sécuriser les merges.

### Référence
Un plan existe déjà: `Q2_GITHUB_WORKFLOW_IMPLEMENTATION_PLAN.md`.

### Checklist rapide
- Protection branches `main` et `version-3`:
  - PR obligatoire
  - 1 approval min
  - pas de push direct
  - conversation resolution obligatoire
- Templates:
  - `.github/ISSUE_TEMPLATE/bug_report.md`
  - `.github/ISSUE_TEMPLATE/feature_request.md` (optionnel mais recommandé)
  - `.github/pull_request_template.md`
- README: section “Code Review Process & Conventions” (déjà présente dans `README.md` ici).

### Critères d’acceptation
- Les templates apparaissent dans GitHub UI.
- Les pushes directs sont bloqués.

---

## Q3 — Créer le microservice `grading-service`
### Objectif
Créer un microservice Spring Boot indépendant pour gérer les notes (CRUD complet) + Swagger + erreurs HTTP standard.

### Modèle de données
Entité `Note`:
- `id: Long` (auto-généré)
- `studentId: Long` (référence étudiant)
- `matiere: String`
- `valeur: Double` (0..20)

### Endpoints proposés (REST)
Sous `/api/notes`:
- `GET /api/notes` (liste)
- `GET /api/notes/{id}`
- `POST /api/notes`
- `PUT /api/notes/{id}`
- `DELETE /api/notes/{id}`
- (Option utile) `GET /api/notes?studentId=123` (filtrage par étudiant)

### Composants techniques
- Spring Web + Validation
- Spring Data JPA + PostgreSQL
- Global exception handling (erreurs propres: 400/404/409/500)
- OpenAPI/Swagger UI (springdoc)

### Gestion d’erreurs (standard)
- 400: validation (`@Valid`) + champs invalides
- 404: note introuvable
- 422/400: valeur note hors [0,20]
- 404 côté grading: étudiant inexistant (après Feign en Q4)

### Critères d’acceptation
- Swagger UI disponible (ex: `/swagger-ui.html` ou `/swagger-ui/index.html` selon config).
- Le CRUD fonctionne en local (hors orchestration) et via docker-compose (après Q7).

---

## Q4 — Eureka + Feign + API Gateway

### 4.1 Eureka Server (`eureka-server`)
#### Objectif
Fournir un registre de services.

#### Étapes
- Créer un projet Spring Boot `eureka-server`.
- Ajouter dépendance Eureka Server.
- Activer `@EnableEurekaServer`.
- Config:
  - port `8761`
  - `eureka.client.register-with-eureka=false`
  - `eureka.client.fetch-registry=false`

#### Critères d’acceptation
- UI Eureka accessible sur `http://localhost:8761`.

### 4.2 Enregistrer les services (clients Eureka)
Services à enregistrer:
- `etudiant-service` (backend existant `rest-spring-api/`)
- `grading-service`
- `api-gateway`

Étapes par service:
- Ajouter dépendance Eureka Client.
- Définir `spring.application.name`:
  - `etudiant-service`, `grading-service`, `api-gateway`
- Pointer `eureka.client.service-url.defaultZone` vers `http://eureka-server:8761/eureka` (docker-compose) et `http://localhost:8761/eureka` (local hors docker).

Critères d’acceptation:
- Les 3 services apparaissent en UP dans Eureka.

### 4.3 Feign (dans `grading-service`)
#### Objectif
Vérifier l’existence d’un étudiant avant création/mise à jour d’une note.

#### Contrat attendu (déjà présent côté étudiant)
- `GET /api/etudiants/{id}` retourne 200 si existe, 404 sinon.

#### Étapes
- Activer Feign (`@EnableFeignClients`).
- Déclarer un client:
  - `@FeignClient(name = "etudiant-service")`
  - méthode `getStudentById(Long id)` ciblant `/api/etudiants/{id}`
- Dans la logique métier de note:
  - avant `create` / `update`, appeler le client
  - si 404 → retourner 404 (ou 400) avec un message clair (ex: "Student not found").

Critères d’acceptation:
- `POST /api/notes` échoue proprement si `studentId` invalide.

### 4.4 API Gateway (`api-gateway`)
#### Objectif
Point d’entrée unique (`8080`) et routage via discovery (`lb://...`).

#### Routes minimales
- `/api/etudiants/**` → `lb://etudiant-service`
- `/api/departements/**` → `lb://etudiant-service`
- `/api/notes/**` → `lb://grading-service`

Exemple `application.yml`:
```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: etudiant-service-etudiants
          uri: lb://etudiant-service
          predicates:
            - Path=/api/etudiants/**
        - id: etudiant-service-departements
          uri: lb://etudiant-service
          predicates:
            - Path=/api/departements/**
        - id: grading-service
          uri: lb://grading-service
          predicates:
            - Path=/api/notes/**
```

#### Points d’attention
- CORS: nécessaire pour le frontend Next.js et les apps mobiles (si appels directs au gateway).
- Swagger: optionnel au niveau gateway; au minimum chaque service expose son Swagger.

Critères d’acceptation
- Tous les endpoints fonctionnent via `http://localhost:8080/...`.

---

## Q5 — Mise à jour de l’application mobile (Flutter ou React Native)
### Objectif
- Charger `GET /api/departements` (via Gateway)
- Sélectionner un département
- Afficher uniquement les étudiants de ce département

### Contrats API
- `GET /api/departements` (liste)
- Pour filtrer étudiants par département, deux options:
  1. **Si un endpoint existe déjà**: `GET /api/etudiants?departementId=...` (recommandé)
  2. **Sinon**: filtrer côté mobile après `GET /api/etudiants` (simple mais moins optimal)

### Stratégie recommandée (propre)
Ajouter côté etudiant-service un endpoint de filtrage (si non présent):
- `GET /api/etudiants?departementId=<id>`

### Implémentation Flutter (si vous choisissez Flutter)
- Écran:
  - `FutureBuilder` pour charger départements
  - `DropdownButton` pour sélection
  - `FutureBuilder`/`setState` pour recharger étudiants du département
- Base URL:
  - en dev local: `http://localhost:8080` (émulateur iOS)
  - Android emulator: `http://10.0.2.2:8080`
  - device réel: `http://<IP_MACHINE>:8080`

### Implémentation React Native (si vous choisissez React Native)
- `useEffect` pour charger départements au mount
- `Picker` (ou UI équivalente) pour sélection
- `FlatList` pour afficher étudiants filtrés
- Base URL: idem Flutter (attention emulator vs device)

### Critères d’acceptation
- Les appels passent par le Gateway.
- La liste d’étudiants change quand le département change.

---

## Q6 — Frontend Next.js dans `frontend/`
### Objectif
Créer un frontend Next.js avec pages dédiées:
- `/etudiants`: liste + formulaire add/edit
- `/departements`: gestion départements

### Contraintes
- Lectures: Server Components
- Formulaires: Client Components (`"use client"`)
- Style: Tailwind CSS
- Toutes les requêtes vont vers l’API Gateway

### API Base URL (recommandé)
- Server-side (dans docker): `API_BASE_URL=http://api-gateway:8080`
- Client-side (dans le navigateur): `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080`

### Structure (cible)
- `frontend/app/etudiants/page.tsx` (Server Component: fetch list)
- `frontend/app/etudiants/[id]/page.tsx` (Server Component: fetch détail)
- `frontend/app/departements/page.tsx` (Server Component: fetch list)
- `frontend/app/components/*` (Client Components pour forms)

### Critères d’acceptation
- Les pages affichent données via Gateway.
- Les créations/updates fonctionnent via formulaires client.

---

## Q7 — Mise à jour `docker-compose.yml` (orchestration complète)
### Objectif
Orchestrer:
- postgres (5432 exposé)
- redis (6379 exposé)
- eureka-server (8761)
- etudiant-service (8081)
- grading-service (8082)
- api-gateway (8080)
- frontend (3000)

### Alignement avec l’existant
Le `docker-compose.yml` actuel contient déjà postgres/redis/rest-spring-api.

### Plan de modifications
1. Renommer le service `rest-spring-api` en `etudiant-service` (ou garder le nom docker-compose, mais définir `spring.application.name=etudiant-service`).
2. Changer le port exposé du service étudiant en `8081:8081`.
3. Ajouter:
   - `eureka-server` build `./eureka-server` port `8761:8761`
   - `grading-service` build `./grading-service` port `8082:8082`
   - `api-gateway` build `./api-gateway` port `8080:8080`
   - `frontend` build `./frontend` port `3000:3000`
4. Ajouter les variables d’environnement:
   - `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka`
   - `SPRING_APPLICATION_NAME=...`
   - Datasource URLs pointant sur `postgres`
5. `depends_on`:
   - services → `eureka-server`
   - etudiant-service → postgres + redis + eureka
   - grading-service → postgres + eureka
   - api-gateway → eureka
   - frontend → api-gateway

### Validation (smoke tests)
- Eureka UI: `http://localhost:8761`
- Gateway:
  - `GET http://localhost:8080/api/departements`
  - `GET http://localhost:8080/api/etudiants`
  - `POST http://localhost:8080/api/notes`
- Frontend: `http://localhost:3000/etudiants`

### Critères d’acceptation
- `docker compose up --build` démarre l’ensemble.
- Les 3 services apparaissent en UP dans Eureka.
- Le Gateway route correctement vers étudiants/départements/notes.

---

## Ordre d’implémentation recommandé (pour réduire les blocages)
1. Q1/Q2 (process)
2. Eureka Server
3. Adapter etudiant-service (enregistrement Eureka + port)
4. API Gateway (routes + CORS)
5. Grading-service (CRUD) puis intégration Eureka
6. Feign dans grading-service
7. Mise à jour docker-compose
8. Frontend Next.js
9. Mobile

---

## Definition of Done (Sprint 3)
- Microservices démarrent via docker-compose et se découvrent via Eureka.
- Les appels externes passent uniquement par `api-gateway`.
- `grading-service` expose CRUD notes + Swagger.
- Mobile: filtrage étudiants par département via Gateway.
- Frontend Next.js: pages etudiants/departements fonctionnelles via Gateway.
