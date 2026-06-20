# User Management API

Identity context service for the Banking Simulation Platform.

## Responsibilities

- users
- roles
- organizations
- profiles

## Architecture rule

This service follows the project backend rules:

```text
Controller -> Facade / UseCase -> Service -> Domain Repository -> JPA Adapter -> JpaRepository
```

## Initial endpoints

```http
POST /users
GET /users/{userId}
```

## Local run

From this folder:

```bash
mvn clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

From the repository root:

```bash
docker compose up --build user-management-api
```

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- JPA adapter layer
- Flyway migration
- first controller tests

Next steps:

- add authentication and authorization
- add organization management
- add role assignment endpoint
- add OpenAPI contract
