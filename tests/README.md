# Automated Local Test Suite

This folder provides a Docker-based local test workflow so the platform can be tested without manually starting services or running Postman by hand.

## What it runs

- PostgreSQL local database
- Kafka local broker
- Fluent Bit local log forwarder
- Observability API
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

This same functional-only mode is used by GitHub Actions on pull requests and pushes to `main`.

## Keep the environment running after tests

```bash
KEEP_ENV=true bash tests/scripts/run-local-tests.sh
```

Then inspect the services manually:

```bash
curl http://localhost:8082/accounts/00000000-0000-0000-0000-000000000000
curl 'http://localhost:8083/operations/accounts/00000000-0000-0000-0000-000000000000?limit=1&offset=0'
```

## Local observability stack

The local Docker environment now includes:

```text
Kafka            localhost:9092
Fluent Bit       localhost:24224
Observability API localhost:8085
```

Core Banking API receives these default Docker values:

```text
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
BANKING_KAFKA_OUTBOX_TOPIC=banking.core.events
BANKING_LOG_FORWARDER_BASE_URL=http://fluent-bit:24224
BANKING_OBSERVABILITY_API_BASE_URL=http://observability-api:8085
```

To validate all outbox destinations locally:

```bash
bash tests/scripts/run-outbox-delivery-tests.sh
```

To validate only one or two destinations:

```bash
DESTINATIONS="KAFKA" bash tests/scripts/run-outbox-delivery-tests.sh
DESTINATIONS="FLUENT_BIT NOOP" bash tests/scripts/run-outbox-delivery-tests.sh
```

The script creates a test account, triggers a credit operation and verifies that the matching outbox row reaches `SENT` for each configured destination.

Supported destination values:

```text
OBSERVABILITY_HTTP
FLUENT_BIT
KAFKA
NOOP
```

Fluent Bit configuration is located in:

```text
observability/fluent-bit/fluent-bit.conf
```

The default Kafka topic used by the application is:

```text
banking.core.events
```

## CI execution

The API check workflow is defined in:

```text
.github/workflows/api-check.yml
```

For now, CI runs the functional Newman/Postman suite only:

```bash
RUN_LOAD=false RUN_SECURITY=false bash tests/scripts/run-local-tests.sh
```

Load and security checks remain available locally and can be promoted to CI once their runtime and stability are acceptable.

## Reports

- Newman JUnit report: `tests/postman/results/newman-results.xml`
- ZAP report: `tests/security/zap-report.html`

## Evolution strategy

This test suite is not frozen. It must evolve with the product.

Every backend PR that adds or changes an API, business rule, error code, security rule, or cross-service dependency should update the automated tests in the same PR.

### Required update rules

- New endpoint: add at least one functional Newman scenario.
- New business rule: add one success case and one failure case.
- New error code: assert the HTTP status and the response `code` field.
- New service dependency: add a failure scenario for unavailable dependency or invalid response.
- New pagination, filtering, or sorting: add boundary tests for minimum, maximum, and default values.
- New security rule: add a security or negative API test.
- New performance-sensitive endpoint: add or extend a k6 scenario.

### Naming convention

Functional test names should follow this pattern:

```text
<Context> - <Behavior> - <Expected result>
```

Examples:

```text
Core - Debit source account - created
Core - Missing Idempotency-Key - rejected
Account - Get missing account - not found
```

### Test ownership

The `tests/` folder is the executable regression suite for the platform. It should be treated like production code:

- reviewed in PRs;
- kept readable;
- updated with each functional change;
- run before merging important backend changes;
- extended before large refactors.

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

### Outbox delivery tests

Outbox delivery checks are defined in:

```text
tests/scripts/run-outbox-delivery-tests.sh
```

They validate the relay flow for configured destinations by checking the outbox status in PostgreSQL.

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
