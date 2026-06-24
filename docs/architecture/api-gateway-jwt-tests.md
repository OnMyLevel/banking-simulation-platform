# API Gateway JWT route tests

## Purpose

The Gateway has a dedicated `jwt` profile for future identity-provider mode. The test suite verifies the route rules attached to that profile before the real provider is wired in deployment.

## Covered behavior

```text
- health endpoint remains public
- account routes require a caller identity
- operation routes require a caller identity
- USER can reach account routes
- ADVISOR can reach operation routes
- OPS cannot use business operation routes
- internal routes stay refused even for ADMIN
```

## Test design

The tests use Spring Security test support to create mock JWT callers in memory.

Backend service URLs point to unavailable local ports. This is intentional:

```text
- if an authorized request reaches routing, the backend call fails with 5xx
- if a route is blocked by Gateway rules, the response is 401 or 403
```

This separates Gateway access behavior from backend availability.

## Expected route matrix in jwt profile

| Scenario | Expected result |
| --- | --- |
| `GET /actuator/health` without caller | `200 OK` |
| `GET /api/accounts/**` without caller | `401 Unauthorized` |
| `GET /api/accounts/**` as `USER` | allowed through Gateway |
| `POST /api/operations/**` as `ADVISOR` | allowed through Gateway |
| `POST /api/operations/**` as `OPS` | `403 Forbidden` |
| `GET /internal/**` as `ADMIN` | `403 Forbidden` |

## Why this matters

These tests lock the target identity route contract before adding real provider configuration and signed-token integration tests.
