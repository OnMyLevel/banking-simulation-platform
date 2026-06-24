# API Gateway traffic store

## Purpose

The Gateway applies per-client traffic budgets before forwarding requests to backend APIs.

The first implementation used local memory. That works for one Gateway instance, but it does not share counters across several Gateway instances.

This guide documents the new store abstraction and the Redis-backed option.

## Store modes

| Mode | Purpose |
| --- | --- |
| `IN_MEMORY` | Default local mode. No external dependency required. |
| `REDIS` | Shared counter mode for several Gateway instances. |

Configuration:

```yaml
banking:
  gateway:
    traffic-store-mode: IN_MEMORY
```

Environment variable:

```text
BANKING_GATEWAY_TRAFFIC_STORE_MODE=IN_MEMORY
```

To use Redis-backed counters:

```text
BANKING_GATEWAY_TRAFFIC_STORE_MODE=REDIS
```

## Runtime behavior

```text
1. Gateway receives a request.
2. TrafficBudgetFilter resolves client key and route family.
3. TrafficBudgetFilter resolves the configured route budget.
4. TrafficBudgetFilter delegates counter storage to TrafficBudgetStore.
5. Store returns allow or reject decision.
6. Gateway forwards allowed requests.
7. Gateway returns 429 with Retry-After when the budget is exceeded.
```

## Redis key strategy

The Redis store uses keys with this shape:

```text
gateway:traffic:<client>:<route-family>
```

Each key expires after one minute. The first request in a new window sets the expiry.

## Why keep IN_MEMORY as default

The local developer experience must stay simple. Developers should be able to run the Gateway without starting Redis.

`IN_MEMORY` remains the default, while production-like deployments can switch to `REDIS`.

## Future integration tests

The next step is to add Testcontainers-based integration tests for the Redis mode.
