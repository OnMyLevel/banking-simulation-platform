# API Gateway operations guide

## Purpose

The API Gateway is the public entry point for the platform. It owns routing, trace headers, route rules, traffic budgets, technical logs and telemetry.

This guide explains which Gateway metrics to watch and how to investigate common issues.

## Metrics to watch

| Metric | Type | Why it matters |
| --- | --- | --- |
| `banking.gateway.requests` | Counter | Tracks request volume by route, method, status and status family. |
| `banking.gateway.request.duration` | Timer | Tracks Gateway latency by route, method and status family. |
| `banking.gateway.traffic.rejections` | Counter | Tracks requests rejected by traffic budgets. |

## Useful Actuator queries

```http
GET /actuator/metrics/banking.gateway.requests
GET /actuator/metrics/banking.gateway.request.duration
GET /actuator/metrics/banking.gateway.traffic.rejections
```

## Dashboard panels

Recommended first dashboard panels:

```text
- total Gateway requests by route family
- Gateway responses by status family
- 401 responses by route family
- 403 responses by route family
- 429 traffic budget rejections by route family
- 5xx responses by route family
- request duration p95 by route family
```

## Route families

| Route family | Paths |
| --- | --- |
| `users` | `/api/users/**` |
| `accounts` | `/api/accounts/**` |
| `operations` | `/api/operations/**` |
| `actuator` | `/actuator/**` |
| `internal` | `/internal/**` |
| `default` | anything else |

## Suggested watch thresholds

These are starting points, not final production values.

| Signal | Initial threshold | First interpretation |
| --- | ---: | --- |
| 401 responses | sudden spike over baseline | Missing bearer value or expired caller identity. |
| 403 responses | sudden spike over baseline | Caller identity exists but role is not allowed. |
| 429 responses | sustained increase for 5 minutes | Traffic budget is actively rejecting callers. |
| 5xx responses | sustained increase for 5 minutes | Backend failure or Gateway routing failure. |
| p95 duration | 2x normal baseline | Slow downstream API or Gateway pressure. |

## Investigation flow

### 1. Start with correlation id

Look for this header in the client response or Gateway log:

```text
X-Correlation-Id
```

Then search Gateway and backend logs using the same value.

### 2. Check status family

Use `banking.gateway.requests` and group by:

```text
route
status
statusFamily
```

Interpretation:

```text
2xx -> request passed successfully
4xx -> caller or route rule issue
5xx -> backend or Gateway failure
```

### 3. Check route family

A problem limited to `operations` usually points to Core Banking API or operation route rules.

A problem limited to `accounts` usually points to Account Banking API or account route rules.

A problem on `users` usually points to User Management API or onboarding routes.

### 4. Check traffic budget rejections

Use:

```http
GET /actuator/metrics/banking.gateway.traffic.rejections
```

If this grows quickly, check:

```text
- route family
- client source
- current budget configuration
- expected traffic pattern
```

### 5. Check latency

Use:

```http
GET /actuator/metrics/banking.gateway.request.duration
```

If Gateway duration rises with 5xx responses, inspect backend availability first.

If Gateway duration rises with 2xx responses, inspect downstream service latency and network pressure.

## Common cases

### Many 401 responses

Likely causes:

```text
- missing Authorization header
- expired bearer value
- wrong environment profile
- identity provider configuration issue
```

Actions:

```text
1. Check affected route family.
2. Verify caller sends bearer access value.
3. Verify jwt profile configuration if enabled.
4. Compare with Gateway signed access tests.
```

### Many 403 responses

Likely causes:

```text
- caller has a valid identity but not the expected role
- role claim is missing
- role claim is not mapped to ROLE_* authority
```

Actions:

```text
1. Check route family.
2. Check expected roles in gateway identity model.
3. Check GatewayClaimAuthoritiesConverter behavior.
4. Confirm the token contains the expected roles claim.
```

### Many 429 responses

Likely causes:

```text
- traffic budget is too low
- caller is retrying too aggressively
- shared traffic store is accumulating counters as expected
```

Actions:

```text
1. Check banking.gateway.traffic.rejections.
2. Check route family.
3. Review current budget values.
4. Confirm Retry-After behavior is visible to clients.
```

### Many 5xx responses

Likely causes:

```text
- backend service unavailable
- wrong target URI
- downstream timeout
- route rewrite mismatch
```

Actions:

```text
1. Check route family.
2. Check target service health.
3. Check Gateway route configuration.
4. Use X-Correlation-Id to find backend logs.
```

## Related documents

```text
docs/architecture/api-gateway-flow.md
docs/architecture/api-gateway-rules.md
docs/architecture/api-gateway-logs.md
docs/architecture/api-gateway-telemetry.md
docs/architecture/api-gateway-tests.md
docs/architecture/api-gateway-jwt-tests.md
docs/architecture/api-gateway-signed-access-tests.md
```
