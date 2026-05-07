# K3s Deployment Guide (Q6) - Student Spring API

This guide deploys your Spring Boot app on a local K3s VM using the Q6 structure:
- `k8s/postgres-deployment.yaml`
- `k8s/etudiant-deployment.yaml`

## 1) Prerequisites on the VM

- K3s is installed and running.
- Docker is installed and you are logged in to Docker Hub.
- You are in the project root:

```bash
cd /home/kadhem/devops-m1/M-Wahid/spring-rest-api-mobile-students-app
```

Use `k3s kubectl` directly (works even if `kubectl` is not configured):

```bash
sudo k3s kubectl get nodes
```

If you want, create an alias for this session:

```bash
alias kubectl='sudo k3s kubectl'
```

## 2) Create a Dockerfile for the Spring API (required once)

Your current root `Dockerfile` is empty, so create it first:

```dockerfile
# File: Dockerfile
FROM maven:3.9.8-eclipse-temurin-21 AS builder
WORKDIR /app

COPY rest-spring-api/pom.xml rest-spring-api/pom.xml
COPY rest-spring-api/.mvn rest-spring-api/.mvn
COPY rest-spring-api/mvnw rest-spring-api/mvnw
COPY rest-spring-api/src rest-spring-api/src

WORKDIR /app/rest-spring-api
RUN chmod +x mvnw && ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/rest-spring-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## 3) Build and push image (Q5 prerequisite for Q6)

Replace `<dockerhub-username>` with your Docker Hub username:

```bash
docker build -t <dockerhub-username>/etudiant-service:1.0 .
docker push <dockerhub-username>/etudiant-service:1.0
```

Optional quick check:

```bash
docker images | grep etudiant-service
```

## 4) Create Kubernetes manifests (Q6)

Create the folder:

```bash
mkdir -p k8s
```

Create `k8s/postgres-deployment.yaml`:

```yaml
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
          value: "postgres"
        - name: POSTGRES_PASSWORD
          value: "postgres"
        - name: POSTGRES_DB
          value: "students_db"
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

Create `k8s/etudiant-deployment.yaml`:

```yaml
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
        image: <dockerhub-username>/etudiant-service:1.0
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:postgresql://postgres:5432/students_db"
        - name: SPRING_DATASOURCE_USERNAME
          value: "postgres"
        - name: SPRING_DATASOURCE_PASSWORD
          value: "postgres"
        - name: SPRING_DATA_REDIS_HOST
          value: "redis"
        - name: SPRING_DATA_REDIS_PORT
          value: "6379"
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

Because your app enables Redis cache, add Redis too:

Create `k8s/redis-deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
      - name: redis
        image: redis:alpine
        ports:
        - containerPort: 6379
---
apiVersion: v1
kind: Service
metadata:
  name: redis
spec:
  selector:
    app: redis
  ports:
  - protocol: TCP
    port: 6379
    targetPort: 6379
```

## 5) Deploy on K3s

```bash
sudo k3s kubectl apply -f k8s/postgres-deployment.yaml
sudo k3s kubectl apply -f k8s/redis-deployment.yaml
sudo k3s kubectl apply -f k8s/etudiant-deployment.yaml
```

Check resources:

```bash
sudo k3s kubectl get pods
sudo k3s kubectl get svc
```

Wait until all pods are `Running`.

## 6) Access the API from your VM host

Get NodePort:

```bash
sudo k3s kubectl get svc etudiant-service
```

Look at the `PORT(S)` column (example: `8080:32080/TCP`).

Then call:

```bash
curl http://<VM_IP>:<NODE_PORT>/api/etudiants
```

Example:

```bash
curl http://192.168.56.10:32080/api/etudiants
```

## 7) Troubleshooting

- Pod not starting:

```bash
sudo k3s kubectl describe pod <pod-name>
sudo k3s kubectl logs <pod-name>
```

- Image pull error:
  - Verify image exists on Docker Hub.
  - Verify image name/tag exactly match the deployment.

- DB connection error:
  - Verify `postgres` pod is `Running`.
  - Verify env vars in `etudiant-deployment.yaml` exactly as above.

- Redis warning/error:
  - Verify `redis` pod/service is deployed and running.

## 8) Clean up

```bash
sudo k3s kubectl delete -f k8s/etudiant-deployment.yaml
sudo k3s kubectl delete -f k8s/redis-deployment.yaml
sudo k3s kubectl delete -f k8s/postgres-deployment.yaml
```

## 9) Suggested Git commit

```bash
git checkout -b version-2
git add K3S_Q6_DEPLOYMENT_GUIDE.md
git commit -m "PROJ-XX: add K3s deployment guide for Q6"
```
