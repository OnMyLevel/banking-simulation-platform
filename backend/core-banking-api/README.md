# Core Banking API

Core banking operation service for the Banking Simulation Platform.

## Responsibilities

- credit operations
- debit operations
- internal transfers
- operation history
- idempotency protection
- audit foundation

## Architecture rule

This service follows the project backend rules:

```text
Controller -> Facade / UseCase -> Service -> Domain Repository -> JPA Adapter -> JpaRepository
```

## Initial endpoints

```http
POST /operations/credits
POST /operations/debits
POST /operations/transfers
GET /operations/accounts/{accountId}
```

Sensitive write endpoints require:

```http
Idempotency-Key: unique-client-operation-key
```

## Business rules covered in foundation

- A credit operation creates one completed operation.
- A debit operation creates one completed operation.
- A transfer operation links a source and target account.
- A duplicated Idempotency-Key returns the existing operation instead of creating a second one.
- Missing Idempotency-Key returns an explicit API error.

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- domain service
- repository port
- JPA adapter layer
- Flyway migration for `core_schema.operations`
- first unit tests for idempotency behavior

## Next steps

- connect to account-banking-api for account status checks
- add balance projection or account ledger
- add insufficient funds rule
- add operation audit events
- add Testcontainers repository integration test
- add OpenAPI contract
