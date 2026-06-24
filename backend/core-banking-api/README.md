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
- reliable event delivery

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

## Internal outbox operations

These endpoints are internal operations endpoints. They must be protected by admin or ops security later.

```http
GET /internal/outbox-events?status=FAILED&limit=25&offset=0
POST /internal/outbox-events/{eventId}/retry
```

The retry endpoint puts the event back in `PENDING` and moves `next_retry_at` to now.

## Event outbox

Core Banking API stores a row in `core_schema.outbox_events` in the same transaction as the banking operation.

A scheduled relay reads pending rows and sends them through a destination-based sender router.

```text
OutboxEventRelay
  -> EventDeliveryRouter
      -> RestEventSender        destination_type = OBSERVABILITY_HTTP
      -> NoopEventSender        destination_type = NOOP
      -> future Kafka sender    destination_type = KAFKA
      -> future Fluent Bit      destination_type = FLUENT_BIT
```

The outbox stores the event type, destination type, payload, retry count, last error and next retry date. This keeps the door open for Kafka, Fluent Bit or another transport later without changing the domain service.

If Observability API is unavailable, the banking operation remains completed. The relay marks the event as failed and retries later.

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

## Event delivery dependency

Default local URL:

```text
http://localhost:8085
```

Docker URL:

```text
http://observability-api:8085
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
- Successful credit, debit and transfer operations create an outbox event through `AuditPublisher`.

## Current status

Implemented foundation:

- Spring Boot project
- domain model
- request and response DTOs
- facade layer
- domain service
- account client port and HTTP adapter
- audit publisher port
- outbox-backed publisher adapter
- destination-based event sender router
- REST sender strategy
- NOOP sender strategy
- scheduled outbox relay
- internal outbox operation endpoints
- HTTP timeout configuration
- account dependency error mapping
- repository port
- JPA adapter layer
- Flyway migration for `core_schema.operations`
- Flyway migration for `core_schema.outbox_events`
- insufficient funds rule
- guarded balance checks
- currency-specific balance projection from operations
- account status checks before credit, debit and transfer
- paginated operation history response
- retryable event delivery to Observability API
- unit tests for idempotency, balance, account status and audit rules
- HTTP account adapter tests
- HTTP sender tests
- EventDeliveryRouter tests
- outbox ops facade tests
- Testcontainers repository integration test
- OpenAPI contract

## Next steps

- secure internal endpoints with admin or ops role
- add Kafka or Fluent Bit sender implementation if the architecture requires it
- add richer OpenAPI examples
