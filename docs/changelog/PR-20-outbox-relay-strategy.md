# PR #20 - feat outbox relay strategy

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/EventSender.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/EventDeliveryRouter.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/RestEventSender.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/NoopEventSender.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventRelay.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/EventDeliveryRouterTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Destination-based outbox delivery strategy.
- Sender interface for event transports.
- Router that selects the sender from `destination_type`.
- REST sender for `OBSERVABILITY_HTTP`.
- NOOP sender for temporary disabled delivery.
- Extension path for future `KAFKA` and `FLUENT_BIT` senders.

## Reasons and goals

The outbox should not be coupled to a single delivery transport. This PR keeps the current HTTP delivery but makes the relay open for Kafka, Fluent Bit or another transport later.

## Architecture and behavior impact

- `OutboxEventRelay` no longer depends directly on the HTTP sender.
- Delivery is selected by `destination_type`.
- New senders can be added without changing banking domain logic.
- Runtime behavior remains HTTP delivery for existing `OBSERVABILITY_HTTP` rows.
