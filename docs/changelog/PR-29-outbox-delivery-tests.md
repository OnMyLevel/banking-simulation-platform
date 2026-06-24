# PR #29 - test outbox delivery checks

## Files changed

- `tests/docker/docker-compose.tests.yml`
- `tests/scripts/run-outbox-delivery-tests.sh`
- `tests/README.md`

## Concepts and features

- Local outbox delivery validation script.
- Observability API added to the local test compose stack.
- Core Banking API receives configurable outbox destination through Docker Compose.
- Validation of `OBSERVABILITY_HTTP`, `FLUENT_BIT`, `KAFKA` and `NOOP` destinations.
- PostgreSQL check that the matching outbox row reaches `SENT`.

## Reasons and goals

The platform now supports multiple outbox delivery strategies. This PR adds a local validation script to prove that a banking operation creates an outbox row and that the relay sends it through the configured destination.

## Architecture and behavior impact

- Does not change runtime business behavior.
- Adds local validation coverage around the outbox relay.
- Prepares future promotion of outbox delivery checks into CI once the Docker stack timing is stable.
