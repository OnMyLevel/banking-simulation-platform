# Account Banking API

Account context service for the Banking Simulation Platform.

## Responsibilities

- bank accounts
- IBAN
- balances
- account history

## Architecture rule

This service follows the project backend rules:

```text
Controller -> Facade / UseCase -> Service -> Domain Repository -> JPA Adapter -> JpaRepository
```

## Initial endpoints

```http
POST /accounts
GET /accounts/{accountId}
```

## Business rules covered in foundation

- An account belongs to one owner.
- An account has a generated IBAN.
- An account is created with a zero balance.
- An account is created as ACTIVE.
- Domain models are separated from JPA entities.
- A missing account returns ACCOUNT_NOT_FOUND with HTTP 404.
- Hibernate validates the schema; Flyway owns schema creation.

## Local run

```bash
mvn clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- JPA adapter layer
- Flyway migration for account_schema.accounts
- account not found exception and error handler
- first unit tests
- first controller test
- first repository integration test with Testcontainers

Next steps:

- add full business exceptions for account status rules
- add owner access control
- add security foundation
- add OpenAPI contract
