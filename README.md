# Banking Simulation Platform

Cloud-native banking simulation platform built for architecture, DDD, microservices, observability and DevOps practice.

## Vision

The goal of this project is to simulate a modern banking platform with a realistic technical architecture:

- Microservices architecture
- Domain Driven Design
- Clean / Hexagonal Architecture
- Event Driven Architecture
- Advanced observability
- Cloud Native deployment
- Infrastructure as Code
- DevOps pipelines

## Target architecture

```text
Frontend Layer
API Gateway
Backend Microservices
Persistence Layer
Observability Layer
DevOps Platform
```

## Main components

### Frontend

- `client-web-react` — Client banking portal
- `business-web-vue` — Business banking portal
- `advisor-admin-angular` — Advisor and admin portal

Frontend integration contract:

```text
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
```

### Backend

- `api-gateway` — single HTTP entry point and service router
- `user-management-api`
- `account-banking-api`
- `core-banking-api`
- `notification-api`
- `observability-api`

### Infrastructure

- Docker
- Docker Compose
- Kubernetes
- Terraform
- GitHub Actions

### Data and observability

- PostgreSQL
- ClickHouse
- Fluent Bit
- Kafka
- MinIO / S3
- Tsuga dashboard

## Repository structure

```text
banking-simulation-platform/
├── backend/
├── frontend/
├── infrastructure/
├── docs/
└── .github/workflows/
```

## Local gateway

Gateway port:

```text
8080
```

Routes:

```text
/api/users/**      -> user-management-api
/api/accounts/**   -> account-banking-api
/api/operations/** -> core-banking-api
```

Health endpoint:

```http
GET http://localhost:8080/actuator/health
```

Gateway documentation:

```text
backend/api-gateway/README.md
docs/architecture/api-gateway-public-contract.md
docs/frontend/gateway-integration-contract.md
```

## MVP v1 scope

The first MVP focuses on:

- Authentication
- User management
- Account management
- Balance consultation
- Deposits
- Withdrawals
- SEPA transfer
- SWIFT transfer
- Instant payment
- Notifications
- Audit
- Docker Compose local environment

## Documentation

See the architecture documentation in [`docs/architecture`](docs/architecture).

## Status

Project initialization in progress.
