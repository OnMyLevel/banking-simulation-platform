# PR #21 - feat retry policy

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxRetryProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxRetryPolicy.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventRelay.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/OutboxRetryPolicyTest.java`

## Concepts and features

- Configurable retry delay.
- Configurable max retry count.
- Configurable batch size.
- Safe defaults when configuration values are missing or invalid.
- Retry policy extracted from the relay.
- Unit tests for retry calculation and max retry behavior.

## Reasons and goals

The relay must be tunable without code changes. HTTP, Kafka and Fluent Bit can need different retry behavior, so this PR moves the policy into a configurable component.

## Architecture and behavior impact

- `OutboxEventRelay` no longer hardcodes batch size or retry delay.
- Retry calculation is isolated in `OutboxRetryPolicy`.
- Events that reach the retry limit are moved to a far future retry date until a later recovery flow is added.
