# Activités d'Intégration de Compétences — Partie 3

## Objectif général

Faire évoluer le projet vers une architecture micro services complète en introduisant la découverte de services (Eureka), la communication inter-services (Feign), une API Gateway, un second micro service de gestion des notes, un frontend Next.js, et une application mobile enrichie. Le tout est orchestré via un `docker-compose.yml` unifié.

---

## Q1 — Créer une branche Git version-3 et ajouter le Sprint 3 dans Jira

Isolez votre travail dans une branche dédiée créée à partir de `version-2` :

```bash
git checkout version-2
git checkout -b version-3
```

Parallèlement, ouvrez votre projet Jira Scrum et créez un **Sprint 3** dans le backlog. Décomposez les questions ci-dessous en User Stories et Tasks avant de commencer le développement — c'est une bonne pratique Agile.

---

## Q2 — Définir le workflow GitHub

Configurez les éléments suivants dans votre dépôt :

- **Protection de branche** : dans `Settings > Branches`, activez la protection sur `main` et `version-3` — exigez au moins une review approuvée avant tout merge, et interdisez les pushs directs.
- **Template d’issue** : créez `.github/ISSUE_TEMPLATE/bug_report.md` (et/ou `feature_request.md`).
- **Template de Pull Request** : créez `.github/pull_request_template.md` avec sections prédéfinies (description, lien Jira, checklist de review).
- **Convention de review** : ajoutez dans votre `README.md` une section décrivant les règles de review (ex. : review sous 48h, résolution des commentaires bloquants avant merge).

---

## Q3 — Créer le micro service grading-service

Créez un nouveau projet Spring Boot indépendant nommé `grading-service`.  
L'entité `Note` comporte les champs suivants :

| Champ       | Type   | Description                          |
|-------------|--------|--------------------------------------|
| id          | Long   | Identifiant auto-généré              |
| studentId   | Long   | Référence vers l'étudiant            |
| matiere     | String | Nom de la matière                    |
| valeur      | Double | Valeur de la note (0-20)             |

Implémentez le CRUD complet avec gestion des erreurs HTTP standard et documentation Swagger.

---

## Q4 — Ajouter Feign, Eureka et API Gateway

- **Eureka** : serveur de registre. Créez `eureka-server` avec `@EnableEurekaServer`. Chaque micro service inclut `spring-cloud-starter-netflix-eureka-client`.
- **Feign** : dans `grading-service`, créez une interface `@FeignClient(name = "etudiant-service")` pour vérifier l’existence d’un étudiant.
- **API Gateway** : créez `api-gateway` avec Spring Cloud Gateway. Exemple de configuration :

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: etudiant-service
          uri: lb://etudiant-service
          predicates:
            - Path=/api/etudiants/**
        - id: grading-service
          uri: lb://grading-service
          predicates:
            - Path=/api/notes/**
```

---

## Q5 — Mettre à jour l’application mobile

L’application mobile (Flutter ou React Native) doit :

- Récupérer la liste des départements via `GET /api/departements`
- Permettre à l’utilisateur de sélectionner un département
- Afficher uniquement les étudiants de ce département

Toutes les requêtes passent désormais par l’**API Gateway**.

- **Flutter** : utilisez `DropdownButton` + `FutureBuilder`
- **React Native** : utilisez `Picker` ou `FlatList` + `useEffect`

---

## Q6 — Créer un frontend Next.js

Créez un projet Next.js dans `frontend/`.  
Pages distinctes :

- `/etudiants` → liste + formulaire d’ajout/édition
- `/departements` → gestion des départements

Utilisez :

- **Server Components** pour les lectures
- **Client Components** (`"use client"`) pour les formulaires
- **Tailwind CSS** pour le style
- Toutes les requêtes pointent vers l’API Gateway

### Structure recommandée

```
frontend/
├── app/
│   ├── etudiants/
│   │   ├── page.tsx
│   │   └── [id]/page.tsx
│   ├── departements/
│   │   └── page.tsx
│   ├── layout.tsx
│   └── components/
│       ├── EtudiantCard.tsx
│       └── DepartementForm.tsx
└── package.json
```

---

## Q7 — Mettre à jour le docker-compose.yml

Orchestration complète avec dépendances :

| Service           | Port exposé | Dépendances                                |
|-------------------|-------------|---------------------------------------------|
| postgres          | 5432        | –                                           |
| redis             | 6379        | –                                           |
| eureka-server     | 8761        | –                                           |
| etudiant-service  | 8081        | postgres, redis, eureka-server             |
| grading-service   | 8082        | postgres, eureka-server                    |
| api-gateway       | 8080        | eureka-server                              |
| frontend          | 3000        | api-gateway                                |

### Extrait

```yaml
services:
  eureka-server:
    build: ./eureka-server
    ports:
      - "8761:8761"
  etudiant-service:
    build: ./api-spring-boot
    ports:
      - "8081:8081"
    depends_on:
      - postgres
      - redis
      - eureka-server
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/etudiants
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka
```

---

## Structure du dépôt GitHub attendue

```
/projet-etudiants/
├── api-spring-boot/       # Micro service étudiant
├── grading-service/       # Micro service notes
├── eureka-server/         # Serveur de registre
├── api-gateway/           # API Gateway
├── frontend/              # Application Next.js
├── mobile-app/            # Application Flutter / React Native
├── k8s/                   # Manifests Kubernetes
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   └── bug_report.md
│   └── pull_request_template.md
└── docker-compose.yml     # Mis à jour avec tous les services
```

> Rappel : chaque fonctionnalité doit faire l’objet d’une Pull Request vers `version-3`, reliée à un ticket Jira du Sprint 3. Profitez du workflow GitHub mis en place en Q2 pour pratiquer la review de code, même en solo.

```

Let me know if you’d like me to generate the actual templates (PR, issue, README review section) or the Docker/Spring configuration files.