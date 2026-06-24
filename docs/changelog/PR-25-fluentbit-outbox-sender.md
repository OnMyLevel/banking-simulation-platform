# PR #25 - feat Fluent Bit outbox sender

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/LogForwarderProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/LogForwarderConfiguration.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/FluentBitEventSender.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/FluentBitEventSenderTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Fluent Bit-compatible outbox sender.
- New destination type: `FLUENT_BIT`.
- Dedicated RestClient for the log forwarder.
- Configuration prefix: `banking.log-forwarder`.
- Unit tests for sender support and HTTP delivery.

## Reasons and goals

The current outbox relay already supports multiple delivery strategies. This PR adds Fluent Bit first, while keeping Kafka for a later PR.

## Architecture and behavior impact

- Existing `OBSERVABILITY_HTTP` behavior is unchanged.
- Events using `destination_type = FLUENT_BIT` can be delivered to a log forwarder HTTP input.
- Kafka remains a future delivery strategy.
