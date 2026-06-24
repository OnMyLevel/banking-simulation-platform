# PR #26 - feat message broker sender

## Files changed

- `backend/core-banking-api/pom.xml`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/KafkaOutboxProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/KafkaOutboxConfiguration.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/KafkaEventSender.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/KafkaEventSenderTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Message broker-compatible outbox sender.
- New destination type: `KAFKA`.
- Configurable topic through `banking.kafka-outbox.topic`.
- Message key based on the outbox aggregate id.
- Message value based on the outbox payload.
- Unit tests for sender support and publishing.

## Reasons and goals

The outbox relay already supports multiple delivery strategies. After adding the log forwarder strategy, this PR adds a broker-based delivery option while keeping HTTP and Fluent Bit unchanged.

## Architecture and behavior impact

- Existing `OBSERVABILITY_HTTP` and `FLUENT_BIT` behavior is unchanged.
- Events using `destination_type = KAFKA` can be published to the configured topic.
- If publishing fails, the relay marks the event as failed and retries later through the existing outbox retry policy.
