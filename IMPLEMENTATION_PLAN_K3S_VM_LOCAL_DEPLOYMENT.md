# Implementation Plan: Next Steps + Local K3s VM Deployment

## Context
- App image already built locally: `kadhemsellami/rest-spring-api:v1.0.0`
- Spring app is container-ready and reads runtime values from env vars:
  - `SERVER_PORT`
  - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
  - `REDIS_HOST`, `REDIS_PORT`
- This plan is execution-ready and intentionally does not execute any command.

## What Is Next (Immediate)
1. Push the image to Docker Hub so K3s can pull it.
2. Create Kubernetes manifests under `k8s/` for namespace, postgres, redis, and app.
3. Apply manifests in dependency order (db/cache first, app second).
4. Expose the app via NodePort (or Ingress if preferred).
5. Validate API and UI endpoints from your host.

## Target Architecture on Local K3s VM
- Namespace: `students`
- PostgreSQL:
  - Deployment + Service (`postgres`)
  - PVC for data persistence
- Redis:
  - Deployment + Service (`redis`)
- Spring app:
  - Deployment + Service (`rest-spring-api`)
  - Image from Docker Hub: `kadhemsellami/rest-spring-api:v1.0.0`
  - Env wired to internal K8s service names (`postgres`, `redis`)

## Planned Manifest Files
Create these files inside `k8s/`:
- `namespace.yaml`
- `postgres-secret.yaml`
- `postgres-pvc.yaml`
- `postgres-deployment.yaml`
- `redis-deployment.yaml`
- `rest-spring-api-deployment.yaml`

## Manifest Content Plan

### 1) Namespace
- `apiVersion: v1`
- `kind: Namespace`
- `metadata.name: students`

### 2) Postgres Secret
- `kind: Secret`
- Keys:
  - `POSTGRES_USER`
  - `POSTGRES_PASSWORD`
  - `POSTGRES_DB`
- Use `stringData` for readability in local setup.

### 3) Postgres PVC
- `kind: PersistentVolumeClaim`
- `accessModes: [ReadWriteOnce]`
- `resources.requests.storage: 1Gi`
- Use default K3s storage class (`local-path`) unless your VM uses another default.

### 4) Postgres Deployment + Service
- Deployment:
  - image: `postgres:16-alpine`
  - containerPort: `5432`
  - env from secret
  - volume mount: `/var/lib/postgresql/data`
- Service:
  - name: `postgres`
  - type: `ClusterIP`
  - port `5432`

### 5) Redis Deployment + Service
- Deployment:
  - image: `redis:7-alpine`
  - containerPort: `6379`
- Service:
  - name: `redis`
  - type: `ClusterIP`
  - port `6379`

### 6) App Deployment + Service
- Deployment:
  - image: `kadhemsellami/rest-spring-api:v1.0.0`
  - `imagePullPolicy: Always`
  - containerPort: `8080`
  - env:
    - `SERVER_PORT=8080`
    - `DB_HOST=postgres`
    - `DB_PORT=5432`
    - `DB_NAME` from secret or explicit value
    - `DB_USERNAME` from secret
    - `DB_PASSWORD` from secret
    - `REDIS_HOST=redis`
    - `REDIS_PORT=6379`
  - readiness probe: `GET /`
  - liveness probe: `GET /actuator/health` only if actuator is enabled; otherwise use `GET /`
- Service:
  - name: `rest-spring-api`
  - type: `NodePort`
  - port: `8080`
  - targetPort: `8080`

## Deployment Order (Run Manually)
1. Create namespace.
2. Create secret and PVC.
3. Deploy postgres and wait until ready.
4. Deploy redis and wait until ready.
5. Deploy spring app.
6. Verify pods and services.
7. Get NodePort and test:
  - `http://<VM_IP>:<NODE_PORT>/`
  - `http://<VM_IP>:<NODE_PORT>/api/etudiants`

## Manual Command Checklist (You Run)
From project root:

1. `docker login`
2. `docker push kadhemsellami/rest-spring-api:v1.0.0`
3. `docker tag kadhemsellami/rest-spring-api:v1.0.0 kadhemsellami/rest-spring-api:latest`
4. `docker push kadhemsellami/rest-spring-api:latest`
5. `sudo k3s kubectl apply -f k8s/namespace.yaml`
6. `sudo k3s kubectl apply -f k8s/postgres-secret.yaml`
7. `sudo k3s kubectl apply -f k8s/postgres-pvc.yaml`
8. `sudo k3s kubectl apply -f k8s/postgres-deployment.yaml`
9. `sudo k3s kubectl apply -f k8s/redis-deployment.yaml`
10. `sudo k3s kubectl apply -f k8s/rest-spring-api-deployment.yaml`
11. `sudo k3s kubectl -n students get pods,svc`
12. `sudo k3s kubectl -n students get svc rest-spring-api`

## Validation Criteria
- Postgres pod is Running and PVC is Bound.
- Redis pod is Running.
- App pod is Running and Ready.
- UI page loads from `/`.
- REST endpoint responds from `/api/etudiants`.

## Troubleshooting Plan
- If app pod crashes:
  - Check pod logs and inspect DB/Redis env values.
- If DB connection fails:
  - Verify `DB_HOST=postgres`, `DB_PORT=5432`, and secret values.
- If image pull fails:
  - Verify Docker Hub image name, tag, and registry visibility.
- If NodePort is unreachable:
  - Check VM network mode and firewall/NAT forwarding.

## Optional Next Improvement
- Add Ingress on K3s Traefik for cleaner URL access instead of NodePort.
- Move sensitive DB values to sealed secrets or external secret manager for non-local environments.
