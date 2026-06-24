# API Gateway route rules

## Purpose

The API Gateway is the entry point for frontend and external clients. It applies shared route rules before requests are sent to backend services.

## Route access matrix

| Route | Rule | Notes |
| --- | --- | --- |
| `/actuator/health` | Public | Used by local and platform health checks. |
| `/actuator/info` | Public | Used for service metadata. |
| `/api/users/**` | Public for MVP | To be refined when login and token issuance are implemented. |
| `/api/accounts/**` | Authenticated | Requires a valid caller at Gateway level. |
| `/api/operations/**` | Authenticated | Requires a valid caller at Gateway level. |
| `/internal/**` | Denied at Gateway | Internal operations must not be exposed through the public entry point. |

## Current authentication model

The current implementation prepares Gateway-level route protection with Spring Security WebFlux.

This is an MVP step. The target model is JWT or OAuth2 resource server validation at Gateway level, followed by service-to-service trust rules for backend APIs.

## Traffic budget model

The Gateway applies an in-memory per-client budget by route family and minute.

Starting values:

| Route family | Requests per minute |
| --- | ---: |
| `/api/users/**` | 120 |
| `/api/accounts/**` | 60 |
| `/api/operations/**` | 30 |
| Default | 180 |

When a caller goes over the budget, the Gateway returns:

```http
HTTP/1.1 429 Too Many Requests
Retry-After: 60
```

## Client identity for traffic budget

The Gateway identifies the caller with this order:

```text
1. first value of X-Forwarded-For if present
2. remote address if available
3. unknown fallback
```

## Algorithm

```text
1. Receive request.
2. Apply correlation id filter.
3. Apply route access rules.
4. Resolve route family from path.
5. Resolve client key from X-Forwarded-For or remote address.
6. Count request inside the current minute bucket.
7. Reject with 429 if the bucket is over budget.
8. Forward allowed request to the selected backend service.
```

## Future improvements

```text
- Replace in-memory budget with Redis-backed distributed counters.
- Add JWT or OAuth2 validation at Gateway level.
- Add route-specific roles.
- Add structured request logging.
- Add dashboard panels for 401, 403 and 429 responses.
```
