FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

COPY src/ src/
RUN mvn -q -DskipTests clean package && \
    JAR_PATH="$(find target -maxdepth 1 -name '*.jar' ! -name '*original*' | head -n 1)" && \
    cp "$JAR_PATH" /workspace/app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /workspace/app.jar /app/app.jar

EXPOSE 8083
USER spring

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
