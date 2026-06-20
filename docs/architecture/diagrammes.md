# Diagrammes d'Architecture

# Banking Simulation Platform

## 1. Diagramme global

```mermaid
flowchart TB
    CLIENT["👤 Client Particulier"]
    BUSINESS["🏢 Entreprise"]
    ADVISOR["👨‍💼 Conseiller Bancaire"]
    ADMIN["👨‍💻 Administrateur"]

    subgraph Frontend Layer
        REACT["Client Banking Portal<br/>React + TypeScript"]
        VUE["Business Banking Portal<br/>Vue.js + TypeScript"]
        ANGULAR["Advisor & Admin Portal<br/>Angular + TypeScript"]
    end

    CLIENT --> REACT
    BUSINESS --> VUE
    ADVISOR --> ANGULAR
    ADMIN --> ANGULAR

    COGNITO["AWS Cognito<br/>OAuth2 / OIDC / JWT"]
    GATEWAY["API Gateway"]

    REACT --> GATEWAY
    VUE --> GATEWAY
    ANGULAR --> GATEWAY
    GATEWAY --> COGNITO

    subgraph Backend Layer
        USER["User Management API"]
        ACCOUNT["Account Banking API"]
        CORE["Core Banking API"]
        NOTIFICATION["Notification API"]
        OBS["Observability API"]
    end

    GATEWAY --> USER
    GATEWAY --> ACCOUNT
    GATEWAY --> CORE
    CORE --> NOTIFICATION

    USER --> OBS
    ACCOUNT --> OBS
    CORE --> OBS
    NOTIFICATION --> OBS

    subgraph Persistence Layer
        POSTGRES["PostgreSQL"]
    end

    USER --> POSTGRES
    ACCOUNT --> POSTGRES
    CORE --> POSTGRES
    NOTIFICATION --> POSTGRES

    subgraph Observability Platform
        FLUENT["Fluent Bit"]
        CLICKHOUSE["ClickHouse"]
        TSUGA["Tsuga"]
        MINIO["MinIO / S3"]
    end

    OBS --> FLUENT
    FLUENT --> CLICKHOUSE
    CLICKHOUSE --> TSUGA
    CLICKHOUSE --> MINIO
```

---

## 2. Architecture des APIs

```mermaid
flowchart TB
    GATEWAY["API Gateway"]
    USER["User Management API"]
    ACCOUNT["Account Banking API"]
    CORE["Core Banking API"]
    NOTIFICATION["Notification API"]
    OBS["Observability API"]

    GATEWAY --> USER
    GATEWAY --> ACCOUNT
    GATEWAY --> CORE
    CORE --> NOTIFICATION

    USER --> OBS
    ACCOUNT --> OBS
    CORE --> OBS
    NOTIFICATION --> OBS
```

---

## 3. Architecture interne d'un microservice

```mermaid
flowchart TB
    CONTROLLER["REST Controller"]
    APP["Application Layer"]
    DOMAIN["Domain Layer"]
    REPOSITORY["Domain Repository"]
    ADAPTER["JPA Adapter"]
    JPA["Spring Data JPA"]
    DB["PostgreSQL"]

    CONTROLLER --> APP
    APP --> DOMAIN
    DOMAIN --> REPOSITORY
    REPOSITORY --> ADAPTER
    ADAPTER --> JPA
    JPA --> DB
```

---

## 4. Bounded Contexts DDD

```mermaid
flowchart LR
    IDENTITY["Identity Context"]
    ACCOUNT["Account Context"]
    BANKING["Banking Context"]
    APPROVAL["Approval Context"]
    NOTIFICATION["Notification Context"]
    OBSERVABILITY["Observability Context"]

    IDENTITY --> ACCOUNT
    ACCOUNT --> BANKING
    BANKING --> APPROVAL
    BANKING --> NOTIFICATION
    IDENTITY --> OBSERVABILITY
    ACCOUNT --> OBSERVABILITY
    BANKING --> OBSERVABILITY
    APPROVAL --> OBSERVABILITY
    NOTIFICATION --> OBSERVABILITY
```

---

## 5. Architecture Kubernetes cible

```mermaid
flowchart TB
    INTERNET["Internet"] --> INGRESS["Ingress Controller"]

    subgraph Kubernetes Cluster
        GATEWAY["API Gateway"]
        USER["User API"]
        ACCOUNT["Account API"]
        CORE["Core API"]
        NOTIFICATION["Notification API"]
        OBS["Observability API"]
        POSTGRES["PostgreSQL"]
        CLICKHOUSE["ClickHouse"]
        MINIO["MinIO"]
        FLUENT["Fluent Bit"]
    end

    INGRESS --> GATEWAY
    GATEWAY --> USER
    GATEWAY --> ACCOUNT
    GATEWAY --> CORE
    CORE --> NOTIFICATION

    USER --> POSTGRES
    ACCOUNT --> POSTGRES
    CORE --> POSTGRES
    NOTIFICATION --> POSTGRES

    USER --> OBS
    ACCOUNT --> OBS
    CORE --> OBS
    NOTIFICATION --> OBS

    OBS --> FLUENT
    FLUENT --> CLICKHOUSE
    CLICKHOUSE --> MINIO
```
