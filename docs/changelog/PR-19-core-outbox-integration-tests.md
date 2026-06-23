# PR #19 - test core outbox integration

## Files changed

- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/OutboxEventJpaAdapterIntegrationTest.java`

## Concepts and features

- PostgreSQL/Testcontainers integration coverage for the outbox repository adapter.
- Persistence verification for outbox event metadata.
- Query coverage for retryable outbox events.
- Validation that `PENDING` and `FAILED` events are selected when ready.
- Validation that `SENT` and future retry events are not selected.
- Ordering check by creation date.

## Reasons and goals

The outbox introduced in PR #18 is a reliability-critical component. This PR locks down the repository behavior against a real PostgreSQL database before adding more relay strategies such as Kafka or Fluent Bit.

## Architecture and behavior impact

- Does not change runtime behavior.
- Strengthens the persistence contract for `core_schema.outbox_events`.
- Gives confidence before adding configurable delivery strategies.
