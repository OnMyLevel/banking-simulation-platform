# Core Banking API

Core banking operation service for the Banking Simulation Platform.

## Responsibilities

- credit operations
- debit operations
- internal transfers
- operation history
- idempotency protection
- balance projection
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

## Business rules covered

- A credit operation creates one completed operation.
- A debit operation creates one completed operation only if the account has enough available balance.
- A transfer operation debits the source account and credits the target account through the same operation record.
- A transfer is rejected when the source account has insufficient funds.
- A duplicated Idempotency-Key returns the existing operation instead of creating a second one.
- Missing Idempotency-Key returns an explicit API error.
- Balance projection is computed from existing operations.

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
- insufficient funds rule
- balance projection from operations
- unit tests for idempotency and balance rules
- Testcontainers repository integration test
- OpenAPI contract

## Next steps

- connect to account-banking-api for account status checks
- add operation audit events
- add richer OpenAPI examples
- expose paginated history parameters at REST level
- update project changelog
