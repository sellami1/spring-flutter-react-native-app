1. Put this content in Dockerfile

  FROM maven:3.9.9-eclipse-temurin-21 AS build
  WORKDIR /workspace

  COPY .mvn .mvn
  COPY rest-spring-api/mvnw rest-spring-api/mvnw
  COPY pom.xml pom.xml

  WORKDIR /workspace/rest-spring-api
  RUN chmod +x mvnw
  RUN ./mvnw -DskipTests dependency:go-offline

  COPY rest-spring-api/src src
  RUN ./mvnw -DskipTests clean package

  FROM eclipse-temurin:21-jre
  WORKDIR /app
  COPY --from=build /workspace/rest-spring-api/target/rest-spring-api-0.0.1-SNAPSHOT.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java","-jar","/app/app.jar"]

2. Build the image from the project root

  cd /home/kadhem/devops-m1/M-Wahid/spring-rest-api-mobile-students-app
  docker build -t YOUR_DOCKERHUB_USERNAME/etudiant-service:1.0 .

3. Login to Docker Hub and push

  docker login
  docker push YOUR_DOCKERHUB_USERNAME/etudiant-service:1.0

4. Optional but recommended: also push latest tag

  docker tag YOUR_DOCKERHUB_USERNAME/etudiant-service:1.0 YOUR_DOCKERHUB_USERNAME/etudiant-service:latest
  docker push YOUR_DOCKERHUB_USERNAME/etudiant-service:latest

5. Quick verify image exists locally

  docker images | grep etudiant-service

Important runtime note
- Your app needs Postgres and Redis.
- docker-compose.yml currently has Redis only, not Postgres or app service.
- So after pulling/running the image, you must either:
1. Use an existing Postgres instance and pass datasource env vars at container run time, or
2. Extend docker-compose.yml to include postgres and the app container.

If you want, I can give you a ready-to-use full compose file (app + postgres + redis) that runs everything with one command.