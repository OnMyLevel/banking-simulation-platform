# PR #43 - feat Gateway telemetry

## Files changed

- `backend/api-gateway/src/main/java/com/banking/gateway/telemetry/GatewayTelemetry.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/logging/GatewayRequestLoggingFilter.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficBudgetFilter.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/telemetry/GatewayTelemetryTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/logging/GatewayRequestLoggingFilterTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/traffic/TrafficBudgetFilterTest.java`
- `docs/architecture/api-gateway-telemetry.md`
- `backend/api-gateway/README.md`

## Features

- Gateway request counter.
- Gateway request duration timer.
- Gateway traffic rejection counter.
- Route family tags: users, accounts, operations, actuator, internal and default.
- HTTP method, status and status family tags.
- Tests for telemetry counters, timers and route family mapping.
- Documentation for dashboard and operational usage.

## Metrics

```text
banking.gateway.requests
banking.gateway.request.duration
banking.gateway.traffic.rejections
```

## Reasons and goals

The Gateway now owns routing, trace headers, route rules, traffic budgets, logs and signed access behavior. This PR adds quantitative visibility through Actuator and Micrometer.

## Architecture and behavior impact

- Adds Micrometer recording from existing Gateway filters.
- Does not change route behavior.
- Prepares dashboard and runbook work.
