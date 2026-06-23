# PR #18 - feat core event outbox

## Files changed

- `backend/core-banking-api/src/main/resources/db/migration/V2__create_core_outbox_events.sql`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/model/OutboxEvent.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/model/OutboxEventStatus.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/repository/OutboxEventRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/**`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/audit/HttpAuditPublisher.java`
- `backend/core-banking-api/src/main/java/com/banking/core/CoreBankingApplication.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Outbox table for reliable event delivery.
- Event rows created in the same transaction as banking operations.
- Scheduled relay for pending and failed events.
- Retry state with retry count, last error and next retry date.
- Flexible destination type to support HTTP today and Kafka or Fluent Bit later.

## Reasons and goals

Direct HTTP delivery can lose events when the downstream observability service is unavailable. The outbox makes event delivery retryable without rolling back completed banking operations.

## Architecture and behavior impact

- Banking operation persistence and event persistence are coupled transactionally.
- Event transport is separated from event creation.
- Current transport remains Observability HTTP.
- Future Kafka or Fluent Bit delivery can be added by introducing a new relay/sender for the same outbox rows.
