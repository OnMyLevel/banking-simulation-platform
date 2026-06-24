# API Gateway shared traffic tests

## Purpose

The Gateway can use a shared counter store for traffic budgets. The tests verify the store behavior without changing local startup defaults.

## Covered behavior

```text
- first request increments the shared counter
- first request sets the one-minute window expiry
- allowed request returns remaining budget
- request above budget is rejected
- retry-after value is returned with the rejection decision
```

## Current test level

The current test uses mocked reactive store operations. This keeps CI fast and avoids requiring an external service in every pull request.

## Future integration level

A later PR can add container-based tests for the shared store when the CI pipeline is ready to start external test services reliably.

## Why this matters

Local counters work for one Gateway instance. Shared counters are required when several Gateway instances run behind a load balancer. This test suite protects the store contract before adding heavier integration tests.
