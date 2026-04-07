Formateur : Wahid Hamdi

# Activité : API REST Spring Boot 4 +

# Application mobile Flutter / React Native

## 🎯 Objectif général

Développer un mini-projet complet composé de :

```
1. Une API REST Spring Boot 4 permettant de gérer une liste d’étudiants.
2. Une base de données PostgreSQL) exécutée via Docker.
3. Un fichier docker-compose.yml permettant d’exécuter l’API + la base de données.
4. Une application mobile Flutter ou React Native consommant l’API et affichant la
liste des étudiants.
5. Un dépôt GitHub contenant tout le code du projet (API, mobile, Docker).
```
# ✅ Partie 1 : Développement de l’API REST

# avec Spring Boot 4

## Description

Créer une application Spring Boot 4 offrant une API REST pour gérer des étudiants.

## Attributs de l'entité Etudiant

```
id (Long, auto-généré)
cin (String)
nom (String)
dateNaissance (LocalDate)
```
## Fonctionnalités minimales

Créer un endpoint :

```
Méthode URL Description
GET /api/etudiants Retourne la liste des étudiants
```
## Exigences techniques

```
Spring Web
Spring Data JPA
Lombok (optionnel)
Base de données PostgreSQL via Docker
Données initiales (au moins 5 étudiants) via un CommandLineRunner ou fichier data.sql
```

Formateur : Wahid Hamdi

# ✅ Partie 2 : Exécution dans Docker

# (Obligatoire)

## ✅ Configuration requise

Vous devez fournir un fichier **Dockerfile** pour l’API Spring Boot :

```
Construction d’une image contenant l’application
Exposition du port 8080
```
Vous devez également fournir un fichier **docker-compose.yml** permettant de démarrer :

* ✅ Le conteneur **base de données** (MySQL ou PostgreSQL)
* ✅ Le conteneur **API Spring Boot**
* ✅ Un réseau Docker permettant la communication entre les deux

## ✅ Le service Spring Boot doit dépendre du service base de

## données :

depends_on:

- db

## ✅ Le lancement doit se faire avec :

```
docker compose up --build
```
L’API doit être accessible à l’adresse :

```
http://localhost:8080/api/etudiants
```
# ✅ Partie 3 : Application mobile Flutter /

# React Native

## Description

Développer une application qui affiche la liste des étudiants obtenue via l’API Dockerisée.

## App Mobile — Exigences

```
Consommer l’API :
http://<IP machine>/api/etudiants
Afficher les champs :
```

Formateur : Wahid Hamdi

### CIN

```
Nom
Date de naissance
```
## Version Flutter (obligations)

```
Utiliser le package http
Créer un modèle Etudiant
Parser le JSON
Afficher la liste dans un ListView.builder
```
## Version React Native (obligations)

```
Utiliser fetch ou axios
Utiliser FlatList
Créer un composant d’affichage
```
# ✅ Partie 4 : Dépôt GitHub obligatoire

Fournir **un lien GitHub unique** contenant :

✅ Le dossier **spring-boot-api/**
✅ Le dossier **mobile-app/** (flutter ou react native)
✅ Le fichier **Dockerfile**
✅ Le fichier **docker-compose.yml**
✅ Un fichier README expliquant comment lancer chaque partie

Structure recommandée :

```
/projet-etudiants/
│
├── api-spring-boot/
│ ├── src/
│ ├── Dockerfile
│ └── pom.xml
│
├── mobile-app/
│ ├── lib/ (Flutter) ou src/ (React Native)
│ └── pubspec.yaml ou package.json
│
└── docker-compose.yml
```