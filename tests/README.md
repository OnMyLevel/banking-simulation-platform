# Automated Local Test Suite

This folder provides a Docker-based local test workflow so the platform can be tested without manually starting services or running Postman by hand.

## What it runs

- PostgreSQL local database
- Account Banking API
- Core Banking API
- Functional API tests with Newman/Postman
- Smoke load test with k6
- Baseline security scan with OWASP ZAP

## Quick start

From the repository root:

```bash
bash tests/scripts/run-local-tests.sh
```

The script starts the local Docker environment, waits for the APIs, runs the functional tests, then runs the load and security checks.

## Faster functional-only run

```bash
RUN_LOAD=false RUN_SECURITY=false bash tests/scripts/run-local-tests.sh
```

## Keep the environment running after tests

```bash
KEEP_ENV=true bash tests/scripts/run-local-tests.sh
```

Then inspect the services manually:

```bash
curl http://localhost:8082/accounts/00000000-0000-0000-0000-000000000000
curl 'http://localhost:8083/operations/accounts/00000000-0000-0000-0000-000000000000?limit=1&offset=0'
```

## Reports

- Newman JUnit report: `tests/postman/results/newman-results.xml`
- ZAP report: `tests/security/zap-report.html`

## Test categories

### Functional tests

Functional tests are defined in:

```text
tests/postman/Banking-Simulation.postman_collection.json
```

Covered scenarios:

- create account
- get account
- credit account
- debit account
- transfer between accounts
- insufficient funds rejection
- missing Idempotency-Key rejection
- paginated operation history

### Load tests

Smoke load test is defined in:

```text
tests/load/core-banking-smoke-load.js
```

Default settings:

```text
5 virtual users
30 seconds
p95 below 750 ms
error rate below 5 percent
```

Override example:

```bash
K6_VUS=10 K6_DURATION=1m bash tests/scripts/run-local-tests.sh
```

### Security tests

The security profile runs OWASP ZAP baseline scan against the Core Banking API history endpoint.

This is a baseline safety check, not a complete penetration test.

## Docker Compose file

```text
tests/docker/docker-compose.tests.yml
```

The services run through Maven containers so the suite does not require building service images first.
