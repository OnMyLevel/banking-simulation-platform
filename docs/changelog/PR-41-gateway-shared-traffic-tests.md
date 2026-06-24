# PR #41 - test Gateway shared traffic store

## Files changed

- `backend/api-gateway/pom.xml`
- `backend/api-gateway/src/test/java/com/banking/gateway/traffic/RedisTrafficBudgetStoreTest.java`
- `docs/architecture/api-gateway-shared-traffic-tests.md`

## Features

- Shared traffic store unit tests added.
- Counter increment behavior verified.
- One-minute window expiry behavior verified.
- Budget rejection behavior verified.
- Shared traffic test strategy documented.

## Reasons and goals

PR #40 added a shared traffic store mode. This PR verifies the store contract without making every CI run depend on an external service.

## Architecture and behavior impact

- Adds tests and documentation only.
- Does not change runtime behavior.
- Keeps heavier container-based checks for a later PR.
