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
- outbox delivery monitoring

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

These endpoints are internal operations endpoints and are protected with Spring Security.

```http
GET /internal/outbox-events?status=FAILED&limit=25&offset=0
POST /internal/outbox-events/{eventId}/retry
```

Access rule:

```text
/internal/** requires OPS or ADMIN role
```

Temporary local users are available for MVP testing only:

```text
ops / ops      role OPS
admin / admin  role ADMIN
```

The target security model is OAuth2/JWT or another centralized identity mechanism. The Core API now includes the OAuth2 Resource Server dependency and centralized role constants to prepare that migration while keeping local Basic Auth active for MVP testing.

The retry endpoint puts the event back in `PENDING` and moves `next_retry_at` to now.

## Event outbox

Core Banking API stores a row in `core_schema.outbox_events` in the same transaction as the banking operation.

A scheduled relay reads pending rows and sends them through a destination-based sender router.

```text
OutboxAuditPublisher
  -> destinationType from banking.outbox.destination-type
  -> outbox_events.destination_type
      -> EventDeliveryRouter
          -> RestEventSender        OBSERVABILITY_HTTP
          -> FluentBitEventSender   FLUENT_BIT
          -> KafkaEventSender       KAFKA
          -> NoopEventSender        NOOP
```

The outbox stores the event type, destination type, payload, retry count, last error and next retry date. This keeps the delivery transport replaceable without changing the domain service.

If Observability API, the log forwarder or Kafka is unavailable, the banking operation remains completed. The relay marks the event as failed and retries later.

## Outbox destination choice

The destination used for newly created outbox events is configurable:

```yaml
banking:
  outbox:
    destination-type: OBSERVABILITY_HTTP
```

Supported values:

```text
OBSERVABILITY_HTTP
FLUENT_BIT
KAFKA
NOOP
```

Recommended usage:

- `OBSERVABILITY_HTTP` for direct integration with the Observability API.
- `FLUENT_BIT` for log-forwarder-first delivery.
- `KAFKA` for event-driven delivery.
- `NOOP` for temporary local disablement.

## Outbox monitoring

The Core API exposes Micrometer metrics through Spring Boot Actuator.

```http
GET /actuator/metrics
```

Useful metrics:

```text
banking.outbox.delivery.success
banking.outbox.delivery.failure
banking.outbox.events{status="PENDING"}
banking.outbox.events{status="FAILED"}
banking.outbox.events{status="SENT"}
```

Suggested operational interpretation:

- `banking.outbox.delivery.failure` increasing quickly means the selected destination is unstable or unavailable.
- `banking.outbox.events{status="FAILED"}` staying high means events need investigation or manual retry.
- `banking.outbox.events{status="PENDING"}` growing continuously means the relay may be blocked or too slow.

## Fluent Bit forwarding

The `FLUENT_BIT` destination is supported by `FluentBitEventSender` through the log forwarder HTTP input.

Default local URL:

```text
http://localhost:24224
```

Default endpoint:

```http
POST /banking.core
```

Configuration prefix:

```yaml
banking:
  log-forwarder:
    base-url: http://localhost:24224
    connect-timeout: 1s
    read-timeout: 2s
```

## Kafka forwarding

The `KAFKA` destination is supported by `KafkaEventSender`.

Default topic:

```text
banking.core.events
```

Configuration prefix:

```yaml
banking:
  kafka-outbox:
    topic: banking.core.events
```

The sender uses the outbox event aggregate id as the Kafka message key and the outbox payload as the message value.

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
- configurable outbox destination
- destination-based event sender router
- outbox delivery metrics
- REST sender strategy
- Fluent Bit sender strategy
- Kafka sender strategy
- NOOP sender strategy
- scheduled outbox relay
- internal outbox operation endpoints
- Spring Security foundation for internal endpoints
- OAuth2 Resource Server dependency prepared
- centralized security role constants
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
- Fluent Bit sender tests
- Kafka sender tests
- configurable destination tests
- EventDeliveryRouter tests
- outbox ops facade tests
- internal endpoint security tests
- Testcontainers repository integration test
- OpenAPI contract

## Next steps

- add operational alerts around outbox failed and pending event counts
- replace temporary local users with a JWT-based resource server configuration
- add richer OpenAPI examples
