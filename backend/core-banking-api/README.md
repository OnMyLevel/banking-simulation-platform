# Core Banking API

Core banking operation service for the Banking Simulation Platform.

## Responsibilities

- credit operations
- debit operations
- internal transfers
- paginated operation history
- idempotency protection
- guarded balance checks
- currency-specific balance projection
- account status validation
- audit event foundation

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
GET /operations/accounts/{accountId}?limit=25&offset=0
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
- Debit and transfer balance checks are protected by an account-level transactional guard.
- A duplicated Idempotency-Key returns the existing operation instead of creating a second one.
- Missing Idempotency-Key returns an explicit API error.
- Balance projection is computed from existing operations and filtered by currency.
- Operation history is paginated with limit, offset and nextOffset.
- Successful credit, debit and transfer operations create an audit event through `AuditPublisher`.

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- domain service
- account client port and HTTP adapter
- audit publisher port
- no-op audit publisher adapter
- HTTP timeout configuration
- account dependency error mapping
- repository port
- JPA adapter layer
- Flyway migration for `core_schema.operations`
- insufficient funds rule
- guarded balance checks
- currency-specific balance projection from operations
- account status checks before credit, debit and transfer
- paginated operation history response
- audit event creation after successful operations
- unit tests for idempotency, balance, account status and audit rules
- HTTP account adapter tests
- Testcontainers repository integration test
- OpenAPI contract

## Next steps

- connect AuditPublisher to observability-api or an event broker
- add richer OpenAPI examples
- add retry policy later if real failure patterns justify it
