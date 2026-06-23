# PR #17 - feat core events HTTP integration

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/AuditClientProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/AuditClientConfiguration.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/EventPayload.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/HttpAuditPublisher.java`
- `backend/core-banking-api/src/main/resources/application.yml`
- `backend/core-banking-api/src/main/resources/application-docker.yml`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/audit/HttpAuditPublisherTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- HTTP adapter for the audit publisher port.
- Configurable Observability API base URL.
- Connect and read timeouts for event delivery.
- Non-blocking event delivery failure behavior.
- Adapter tests for success and remote failure.

## Reasons and goals

Core Banking API already creates audit events. This PR connects those events to Observability API while keeping the domain layer behind the `AuditPublisher` port.

## Architecture and behavior impact

- Replaces the no-op publisher with an HTTP adapter.
- Keeps successful banking operations independent from Observability API availability for the MVP.
- Logs event delivery failures without rolling back the operation.
- Prepares the next step: reliable outbox and retry.
