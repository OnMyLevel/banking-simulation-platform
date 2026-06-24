# PR #28 - chore local observability stack

## Files changed

- `docker-compose.yml`
- `tests/docker/docker-compose.tests.yml`
- `observability/fluent-bit/fluent-bit.conf`
- `tests/README.md`

## Concepts and features

- Local Kafka broker for outbox testing.
- Local Fluent Bit HTTP input for log-forwarder testing.
- Docker Compose wiring for Core Banking API to use Kafka and Fluent Bit.
- Documentation for testing `OBSERVABILITY_HTTP`, `FLUENT_BIT`, `KAFKA` and `NOOP` outbox destinations.

## Reasons and goals

The application can now route outbox events to HTTP, Fluent Bit, Kafka or NOOP. This PR adds the local infrastructure needed to validate those delivery choices outside unit tests.

## Architecture and behavior impact

- Adds local Kafka on port `9092`.
- Adds local Fluent Bit on port `24224`.
- Keeps existing services unchanged.
- Prepares local end-to-end validation of outbox delivery strategies.
