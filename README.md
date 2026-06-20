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

### Backend

- `api-gateway`
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
