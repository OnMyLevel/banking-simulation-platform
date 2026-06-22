# PR #15 - feat core audit events

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/domain/model/AuditEvent.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/port/AuditPublisher.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/service/CoreBankingService.java`
- `backend/core-banking-api/src/main/java/com/banking/core/application/facade/CoreBankingFacade.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/NoOpAuditPublisher.java`
- `backend/core-banking-api/src/test/java/com/banking/core/domain/service/CoreBankingServiceTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Audit event domain model.
- Audit publisher domain port.
- No-op audit publisher adapter.
- Audit event creation after successful credit, debit and transfer operations.
- Unit tests for audit event publishing and non-publishing on rejected operations.

## Reasons and goals

Core banking operations must be traceable. This PR adds the audit foundation without coupling the domain service to a concrete observability implementation.

## Architecture and behavior impact

- Keeps audit publishing behind a domain port.
- Preserves the clean architecture boundary.
- Prepares future integration with observability-api or an event broker.
- Does not add external runtime infrastructure yet.
