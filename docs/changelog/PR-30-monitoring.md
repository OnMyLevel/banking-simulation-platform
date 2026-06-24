# PR #30 - feat monitoring for outbox flow

## Files changed

- `backend/core-banking-api/pom.xml`
- `backend/core-banking-api/src/main/resources/application.yml`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/repository/OutboxEventRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventJpaRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventJpaAdapter.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxDeliveryMetrics.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/EventDeliveryRouter.java`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/outbox/EventDeliveryRouterTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Spring Boot Actuator support for Core Banking API metrics.
- Micrometer counters for send success and send error.
- Micrometer gauges for outbox event counts by status.
- Repository count operation by outbox status.
- Documentation of operational metrics.

## Metrics added

```text
banking.outbox.delivery.success
banking.outbox.delivery.failure
banking.outbox.events{status="PENDING"}
banking.outbox.events{status="FAILED"}
banking.outbox.events{status="SENT"}
```

## Reasons and goals

The platform now supports multiple outbox destinations and local delivery checks. This PR adds monitoring signals so an operator can detect queue growth or repeated send errors.

## Architecture and behavior impact

- Does not change banking business behavior.
- Adds observability around the outbox relay path.
- Exposes the Actuator `metrics` endpoint.
- Keeps PostgreSQL as the source of truth for event status counts.
