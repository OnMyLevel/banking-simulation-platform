# API Gateway tests

## Purpose

The API Gateway owns several cross-cutting behaviors. Its tests verify the entry-point contract before requests reach backend services.

## Behaviors covered

```text
- health endpoint exposure
- private path rejection
- MVP public user routes
- account route gate
- operation route gate
- correlation id returned on Gateway responses
```

## Current route checks

| Scenario | Expected result |
| --- | --- |
| `GET /actuator/health` | `200 OK` |
| `GET /internal/outbox-events` | `403 Forbidden` |
| `GET /api/users/**` | allowed through Gateway |
| `GET /api/accounts/**` | `401 Unauthorized` without caller identity |
| `POST /api/operations/**` | `401 Unauthorized` without caller identity |
| public route with `X-Correlation-Id` | same value returned |
| public route without `X-Correlation-Id` | generated value returned |

## Test design

The route behavior test starts the Gateway on a random port and points backend service URLs to unavailable local ports.

This is intentional:

```text
- Gateway rules are tested without starting all backend APIs.
- A public route is considered allowed when it reaches the routing layer and fails only because the backend is unavailable.
- Gated routes must be rejected before any backend call.
```

## Why this matters

Gateway behavior is now bigger than simple routing. It includes request tracing, route rules, traffic budget checks and preparation for an identity provider.

These tests reduce the risk of breaking the public entry point while future PRs add provider configuration, distributed traffic counters and stronger route tests.

## Future tests

```text
- verify route rewrite behavior with stub backend services
- verify authenticated access with signed tokens
- verify role-specific access rules
- verify traffic budget behavior through HTTP-level tests
- verify log fields through appender-based tests if needed
```
