# Implementation Plan — Partie 3 (Q3 + Q4 + Q7)

## Scope
Ce plan couvre uniquement les 3 blocs techniques suivants de `activity_part3.md`:
- **Q3**: création du microservice `grading-service` (CRUD notes + Swagger + erreurs HTTP)
- **Q4**: mise en place **Eureka + Feign + API Gateway**
- **Q7**: mise à jour de `docker-compose.yml` pour orchestrer l’ensemble

> Remarque: ce plan n’inclut pas Q5 (mobile) ni Q6 (Next.js). Il se concentre sur le cœur “microservices + réseau + orchestration”.

---

## Contexte repo (patterns à réutiliser)
Le backend existant `rest-spring-api/` fournit déjà:
- Swagger/OpenAPI via `springdoc` + une config dédiée.
- Une gestion d’erreurs standard via un `@RestControllerAdvice`.

Objectif: appliquer ces mêmes conventions (Swagger + erreurs HTTP) à `grading-service`, et rendre les services découvrables via Eureka.

---

## Décisions / Hypothèses à valider
1. **Service étudiant**
   - Le projet existant `rest-spring-api/` jouera le rôle de **etudiant-service**.
   - On ne renomme pas le dossier, mais on configure `spring.application.name=etudiant-service`.

2. **Ports (conformes à l’énoncé)**
   - `etudiant-service`: 8081
   - `grading-service`: 8082
   - `api-gateway`: 8080
   - `eureka-server`: 8761

3. **Compatibilité Spring Boot / Spring Cloud**
   - Le backend existant utilise Spring Boot `4.0.5`.
   - Action obligatoire du plan: **verrouiller une combinaison compatible** Spring Boot ↔ Spring Cloud pour Eureka/Feign/Gateway.
   - Deux options:
     - Option A (souvent la plus simple): migrer les microservices vers un couple Boot 3.x + Spring Cloud compatible.
     - Option B: conserver Boot 4.x uniquement si une stack Spring Cloud compatible est confirmée.

4. **Base de données**
   - Pour le dev local, on garde un seul conteneur Postgres.
   - Chaque service a son schéma/tables dans la même DB (simple) ou une DB séparée (plus “microservices”).

---

## Q4.1 — `eureka-server`

### Deliverables
- Dossier `eureka-server/` (Spring Boot)
- `application.yml`/`application.properties`
- Port `8761`

### Configuration attendue
- Annotation: `@EnableEurekaServer`
- Propriétés minimales:
  - `server.port=8761`
  - `eureka.client.register-with-eureka=false`
  - `eureka.client.fetch-registry=false`

### Acceptance Criteria
- `http://localhost:8761` affiche l’UI Eureka.

---

## Q4.2 — Transformer `rest-spring-api/` en `etudiant-service` (client Eureka)

### Deliverables
- Dépendance `spring-cloud-starter-netflix-eureka-client`
- Config:
  - `spring.application.name=etudiant-service`
  - `server.port=8081`
  - `eureka.client.service-url.defaultZone=...`

### Acceptance Criteria
- Le service s’enregistre dans Eureka comme `ETUDIANT-SERVICE` (UP).
- Les endpoints existants restent identiques:
  - `/api/etudiants/**`
  - `/api/departements/**`

---

## Q3 — `grading-service` (CRUD + Swagger + erreurs HTTP)

### Deliverables
- Dossier `grading-service/` (Spring Boot)
- Entité `Note` + repository + service + controller
- Swagger UI (springdoc)
- Gestion d’erreurs HTTP “standard” (400/404/409/500)

### Modèle
`Note`:
- `id: Long` (auto)
- `studentId: Long`
- `matiere: String`
- `valeur: Double` (contrainte: 0..20)

### API (CRUD) attendue
Sous `/api/notes`:
- `GET /api/notes`
- `GET /api/notes/{id}`
- `POST /api/notes`
- `PUT /api/notes/{id}`
- `DELETE /api/notes/{id}`

Recommandé (utile mais optionnel):
- `GET /api/notes?studentId=...`

### Validation & erreurs
- Bean Validation:
  - `matiere` non vide
  - `valeur` dans [0,20]
  - `studentId` non null
- Erreurs “propres”:
  - 400 si validation échoue
  - 404 si note non trouvée
  - 409 si conflit (contrainte DB)

### Swagger
- Activer springdoc, exposer Swagger UI.

### Acceptance Criteria
- Les endpoints répondent avec statuts corrects.
- Swagger affiche le contrat API.

---

## Q4.3 — `grading-service` en Eureka client

### Deliverables
- Dépendance eureka-client
- `spring.application.name=grading-service`
- `server.port=8082`

### Acceptance Criteria
- `grading-service` apparaît en UP dans Eureka.

---

## Q4.4 — Feign (dans `grading-service`) vers `etudiant-service`

### Objectif
Avant de créer/modifier une note, vérifier que l’étudiant existe via le microservice étudiant.

### Deliverables
- Dépendance `spring-cloud-starter-openfeign`
- Activation Feign (`@EnableFeignClients`)
- Client:
  - `@FeignClient(name = "etudiant-service")`
  - `GET /api/etudiants/{id}`

### Règle métier
- `POST /api/notes` et `PUT /api/notes/{id}`:
  - appeler Feign avec `studentId`
  - si étudiant absent (404), refuser l’opération avec une erreur claire (404 ou 400 selon convention choisie)

### Acceptance Criteria
- Impossible de créer une note avec un `studentId` inexistant (retour HTTP cohérent + message explicite).

---

## Q4.5 — `api-gateway` (Spring Cloud Gateway)

### Deliverables
- Dossier `api-gateway/` (Spring Cloud Gateway)
- Enregistrement Eureka (client)
- Routage via service discovery

### Routes attendues (minimum)
- `/api/etudiants/**` → `lb://etudiant-service`
- `/api/departements/**` → `lb://etudiant-service`
- `/api/notes/**` → `lb://grading-service`

### CORS (nécessaire)
- Autoriser au moins le frontend (port 3000) et, en dev, les émulateurs mobile.

### Acceptance Criteria
- Via `http://localhost:8080`:
  - `GET /api/departements` route vers etudiant-service
  - `GET /api/etudiants` route vers etudiant-service
  - `GET /api/notes` route vers grading-service

---

## Q7 — Mise à jour `docker-compose.yml`

### Objectif
Démarrer localement tout le système en une commande, avec les dépendances correctes.

### Deliverables
- Mise à jour de `docker-compose.yml` (root)
- Ajout de services:
  - `eureka-server` (8761)
  - `etudiant-service` (8081)
  - `grading-service` (8082)
  - `api-gateway` (8080)
  - (frontend 3000 est Q6 — hors scope de ce plan)

### Environnements requis (minimum)
- Tous les clients:
  - `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka`
- Etudiant-service et grading-service:
  - config datasource vers `postgres:5432`
- Etudiant-service:
  - config redis vers `redis:6379`

### Dépendances (`depends_on`)
- `etudiant-service` dépend de `postgres`, `redis`, `eureka-server`
- `grading-service` dépend de `postgres`, `eureka-server`
- `api-gateway` dépend de `eureka-server`

### Healthchecks
Recommandé:
- Conserver les healthchecks existants postgres/redis.
- Ajouter un healthcheck HTTP pour les services Spring si actuator est disponible.

### Acceptance Criteria
- `docker compose up --build` démarre:
  - postgres + redis + eureka-server + etudiant-service + grading-service + api-gateway
- Eureka UI montre au moins:
  - `ETUDIANT-SERVICE` UP
  - `GRADING-SERVICE` UP
  - `API-GATEWAY` UP
- Les routes fonctionnent via le gateway.

---

## Command Plan (à exécuter manuellement, plus tard)
Depuis la racine du projet:
- Build & run:
  - `docker compose up --build`
- Vérification:
  - `curl -i http://localhost:8761`
  - `curl -i http://localhost:8080/api/departements`
  - `curl -i http://localhost:8080/api/etudiants`
  - `curl -i http://localhost:8080/api/notes`

---

## Definition of Done (Q3 + Q4 + Q7)
- Le système est “microservices-ready” en local via docker-compose.
- Discovery fonctionne (Eureka).
- Le gateway est l’unique point d’entrée externe (8080).
- `grading-service` fournit CRUD notes + Swagger + erreurs HTTP standard.
- `grading-service` valide `studentId` via Feign et refuse les notes pour étudiant inexistant.
