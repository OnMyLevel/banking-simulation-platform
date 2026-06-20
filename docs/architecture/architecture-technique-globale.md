# Architecture Technique Globale

# Banking Simulation Platform

## 1. Vision du projet

La **Banking Simulation Platform** est une plateforme bancaire moderne développée dans un objectif pédagogique, architectural et démonstratif.

Elle permet de simuler le fonctionnement d'une banque de détail en mettant en œuvre :

- Architecture Microservices
- Domain Driven Design
- Clean Architecture
- Hexagonal Architecture
- Event Driven Architecture
- Observabilité avancée
- Cloud Native
- DevOps
- Infrastructure as Code

L'objectif est de démontrer une architecture proche des standards utilisés dans les banques et fintech modernes.

---

## 2. Couches d'architecture

```text
Frontend Layer
API Layer
Domain Layer
Persistence Layer
Observability Layer
DevOps Layer
```

---

## 3. Diagramme global

```mermaid
flowchart TB

    CLIENT["👤 Client"]
    BUSINESS["🏢 Entreprise"]
    ADVISOR["👨‍💼 Conseiller"]
    ADMIN["👨‍💻 Administrateur"]

    subgraph Frontend
        REACT["React Portal"]
        VUE["Vue Portal"]
        ANGULAR["Angular Portal"]
    end

    CLIENT --> REACT
    BUSINESS --> VUE
    ADVISOR --> ANGULAR
    ADMIN --> ANGULAR

    COGNITO["AWS Cognito"]
    GATEWAY["API Gateway"]

    REACT --> GATEWAY
    VUE --> GATEWAY
    ANGULAR --> GATEWAY
    GATEWAY --> COGNITO

    subgraph Backend
        USER["User Management API"]
        ACCOUNT["Account Banking API"]
        CORE["Core Banking API"]
        NOTIF["Notification API"]
        OBS["Observability API"]
    end

    GATEWAY --> USER
    GATEWAY --> ACCOUNT
    GATEWAY --> CORE

    CORE --> NOTIF

    USER --> OBS
    ACCOUNT --> OBS
    CORE --> OBS
    NOTIF --> OBS

    POSTGRES["PostgreSQL"]

    USER --> POSTGRES
    ACCOUNT --> POSTGRES
    CORE --> POSTGRES
    NOTIF --> POSTGRES

    FLUENT["Fluent Bit"]
    CLICKHOUSE["ClickHouse"]
    TSUGA["Tsuga"]
    MINIO["MinIO / S3"]

    OBS --> FLUENT
    FLUENT --> CLICKHOUSE
    CLICKHOUSE --> TSUGA
    CLICKHOUSE --> MINIO
```

---

## 4. Architecture Frontend

La plateforme utilise une approche multi-frontends :

| Application | Technologie | Utilisateurs |
|---|---|---|
| Client Banking Portal | React, TypeScript, Vite | Clients particuliers |
| Business Banking Portal | Vue.js, TypeScript, Pinia | Entreprises |
| Advisor & Admin Portal | Angular, Angular Material, RxJS | Conseillers et administrateurs |

---

## 5. Architecture Backend

Le backend est organisé autour des APIs suivantes :

| API | Bounded Context | Responsabilités |
|---|---|---|
| API Gateway | Transverse | Routage, sécurité, logging |
| User Management API | Identity Context | Utilisateurs, rôles, entreprises |
| Account Banking API | Account Context | Comptes, IBAN, soldes, historique |
| Core Banking API | Banking + Approval Contexts | Dépôts, retraits, virements, validations |
| Notification API | Notification Context | Notifications, emails, templates |
| Observability API | Observability Context | Audit, logs, traces, métriques |

---

## 6. Architecture interne des APIs

Chaque API suit une architecture hexagonale.

```mermaid
flowchart TB
    CONTROLLER["REST Controller"]
    APPLICATION["Application Layer"]
    DOMAIN["Domain Layer"]
    REPOSITORY["Repository Interface"]
    ADAPTER["JPA Adapter"]
    JPA["Spring Data JPA"]
    DATABASE["PostgreSQL"]

    CONTROLLER --> APPLICATION
    APPLICATION --> DOMAIN
    DOMAIN --> REPOSITORY
    REPOSITORY --> ADAPTER
    ADAPTER --> JPA
    JPA --> DATABASE
```

Objectif : protéger le domaine métier des détails techniques.

---

## 7. Architecture Observabilité

L'observabilité est transverse et indépendante du métier.

Elle couvre :

- audit métier ;
- logs techniques ;
- traces distribuées ;
- métriques ;
- archivage long terme.

```mermaid
flowchart LR
    USER["User API"] --> FLUENT
    ACCOUNT["Account API"] --> FLUENT
    CORE["Core API"] --> FLUENT
    NOTIF["Notification API"] --> FLUENT

    FLUENT["Fluent Bit"] --> CLICKHOUSE["ClickHouse"]
    CLICKHOUSE --> TSUGA["Tsuga"]
    CLICKHOUSE --> MINIO["MinIO / S3"]
```

---

## 8. Architecture Event Driven

Kafka sera introduit à partir du MVP 2.

```mermaid
flowchart TB
    USER["User API"] --> KAFKA["Kafka"]
    ACCOUNT["Account API"] --> KAFKA
    CORE["Core API"] --> KAFKA
    KAFKA --> NOTIF["Notification API"]
    KAFKA --> OBS["Observability API"]
```

---

## 9. Sécurité

La sécurité cible repose sur :

- OAuth2 ;
- OpenID Connect ;
- JWT ;
- AWS Cognito ;
- RBAC.

Rôles :

```text
ROLE_CLIENT
ROLE_BUSINESS
ROLE_ADVISOR
ROLE_ADMIN
```

---

## 10. Technologies retenues

| Domaine | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Flyway, OpenAPI |
| Frontend | React, Vue.js, Angular, TypeScript |
| Données | PostgreSQL, ClickHouse, MinIO |
| Observabilité | Fluent Bit, Tsuga |
| DevOps | Docker, Kubernetes, Terraform, GitHub Actions |

---

## 11. Objectif final

Construire une plateforme bancaire simulée capable de démontrer :

- DDD ;
- Architecture hexagonale ;
- microservices ;
- sécurité OAuth2/OIDC ;
- observabilité moderne ;
- cloud native ;
- CI/CD ;
- infrastructure as code.
