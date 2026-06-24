# PR #27 - feat configurable outbox destination

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxDestinationProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxAuditPublisher.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/OutboxAuditPublisherTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Configurable outbox destination for newly created events.
- New property: `banking.outbox.destination-type`.
- Default destination remains `OBSERVABILITY_HTTP`.
- Supported destinations: `OBSERVABILITY_HTTP`, `FLUENT_BIT`, `KAFKA`, `NOOP`.
- Tests for default, Kafka and Fluent Bit destination choices.

## Reasons and goals

HTTP, Fluent Bit and Kafka senders are now available, but the publisher still needed a configurable way to choose the destination for new outbox events. This PR makes the delivery target configurable without changing domain code.

## Architecture and behavior impact

- Existing behavior remains unchanged by default.
- Changing `banking.outbox.destination-type` controls which sender the relay uses later.
- The domain service remains independent from transport choices.
