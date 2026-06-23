# Observability API

Observability service for the Banking Simulation Platform.

## Responsibilities

- receive audit events from banking services
- store received events in PostgreSQL
- prepare future observability and audit views

## Architecture rule

This service follows the project backend rules:

```text
Controller -> Facade / UseCase -> Service -> Domain Repository -> JPA Adapter -> JpaRepository
```

## Endpoint

```http
POST /events
```

Example payload:

```json
{
  "eventId": "11111111-1111-1111-1111-111111111111",
  "sourceAccountId": "22222222-2222-2222-2222-222222222222",
  "targetAccountId": "33333333-3333-3333-3333-333333333333",
  "eventKind": "TRANSFER",
  "eventStatus": "COMPLETED",
  "amount": 10.00,
  "currency": "EUR",
  "eventKey": "client-key-1",
  "occurredAt": "2026-06-22T10:00:00Z"
}
```

## Idempotency

`eventId` is unique. If the same event is received again, the service returns the already stored event instead of creating a second row.

## Validation

Payload constraints match the database limits:

```text
eventKind: required, max 32
eventStatus: required, max 32
eventKey: required, max 128
currency: required, exactly 3
```

## Persistence

Events are stored in:

```text
observability_schema.audit_events
```

Flyway owns the schema creation. Hibernate uses `ddl-auto: validate` only.

## Current status

Implemented foundation:

- Spring Boot service
- event receive endpoint
- request and response DTOs
- facade layer
- domain service
- domain repository port
- JPA adapter layer
- Flyway migration for `observability_schema.audit_events`
- unique constraint on `event_id`
- idempotent receive behavior
- CI build inclusion

## Next steps

- connect Core Banking API `AuditPublisher` to this service
- add query endpoints for audit history
- add retention and filtering rules
- add security once authentication is introduced
