# Core Banking API

Core banking operation service for the Banking Simulation Platform.

## Responsibilities

- credit operations
- debit operations
- internal transfers
- operation history
- idempotency protection
- guarded balance checks
- currency-specific balance projection
- account status validation
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

## Account dependency

Core Banking API calls Account Banking API before executing write operations.

Default local URL:

```text
http://localhost:8082
```

Docker URL:

```text
http://account-banking-api:8082
```

Configured client defaults:

```text
connect-timeout: 1s
read-timeout: 2s
```

## Business rules covered

- A credit operation requires the target account to be ACTIVE.
- A debit operation requires the source account to be ACTIVE.
- A transfer operation requires source and target accounts to be ACTIVE.
- A debit operation creates one completed operation only if the account has enough available balance in the requested currency.
- A transfer operation debits the source account and credits the target account through the same operation record.
- A transfer is rejected when the source account has insufficient funds in the requested currency.
- Debit and transfer balance checks are protected by an account-level transactional guard.
- A duplicated Idempotency-Key returns the existing operation instead of creating a second one.
- Missing Idempotency-Key returns an explicit API error.
- Balance projection is computed from existing operations and filtered by currency.

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- domain service
- account client port and HTTP adapter
- HTTP timeout configuration
- account dependency error mapping
- repository port
- JPA adapter layer
- Flyway migration for `core_schema.operations`
- insufficient funds rule
- guarded balance checks
- currency-specific balance projection from operations
- account status checks before credit, debit and transfer
- unit tests for idempotency, balance and account status rules
- HTTP account adapter tests
- Testcontainers repository integration test
- OpenAPI contract

## Next steps

- add operation audit events
- add richer OpenAPI examples
- expose paginated history parameters at REST level
- update project changelog
- add retry policy later if real failure patterns justify it
