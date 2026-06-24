# API Gateway telemetry

## Purpose

The Gateway owns cross-cutting entry-point behavior: routing, trace headers, route rules, traffic budgets, logs and identity checks.

This guide documents the Gateway metrics exposed through Actuator and Micrometer.

## Metrics

| Metric | Type | Meaning |
| --- | --- | --- |
| `banking.gateway.requests` | Counter | Number of Gateway exchanges. |
| `banking.gateway.request.duration` | Timer | Gateway exchange duration. |
| `banking.gateway.traffic.rejections` | Counter | Number of traffic budget rejections. |

## Request counter tags

`banking.gateway.requests` uses these tags:

| Tag | Example |
| --- | --- |
| `route` | `users`, `accounts`, `operations`, `actuator`, `internal`, `default` |
| `method` | `GET`, `POST` |
| `status` | `200`, `401`, `403`, `429`, `500` |
| `statusFamily` | `2xx`, `3xx`, `4xx`, `5xx`, `other` |

## Duration timer tags

`banking.gateway.request.duration` uses these tags:

| Tag | Example |
| --- | --- |
| `route` | `accounts` |
| `method` | `POST` |
| `statusFamily` | `2xx` |

## Traffic rejection counter tags

`banking.gateway.traffic.rejections` uses these tags:

| Tag | Example |
| --- | --- |
| `route` | `operations` |

## Useful checks

```text
GET /actuator/metrics/banking.gateway.requests
GET /actuator/metrics/banking.gateway.request.duration
GET /actuator/metrics/banking.gateway.traffic.rejections
```

## Dashboard ideas

```text
- total Gateway requests by route family
- 4xx responses by route family
- 401 and 403 access denials
- 429 traffic budget rejections
- request duration p95 by route family
- backend-facing 5xx responses
```

## Operational notes

A spike in `401` usually means missing caller identity.

A spike in `403` usually means the caller is authenticated but not allowed for the route.

A spike in `429` means traffic budget is being enforced.

A spike in `5xx` often means the request passed Gateway rules but failed downstream.
