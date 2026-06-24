# PR #37 - test Gateway route behavior

## Files changed

- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewayRouteBehaviorTest.java`
- `docs/architecture/api-gateway-tests.md`
- `backend/api-gateway/README.md`

## Concepts and features

- HTTP-level Gateway route behavior tests.
- Public health endpoint verification.
- Private path rejection verification.
- MVP user route accessibility verification.
- Account and operation route gate verification.
- Correlation id response behavior verification.
- Gateway test strategy documentation.

## Tested scenarios

```text
GET /actuator/health                 -> 200 OK
GET /internal/outbox-events          -> 403 Forbidden
GET /api/users/**                    -> allowed through Gateway
GET /api/accounts/**                 -> 401 Unauthorized without caller identity
POST /api/operations/**              -> 401 Unauthorized without caller identity
GET /api/users/** with correlation id -> same header returned
GET /api/users/** without header       -> generated header returned
```

## Reasons and goals

The Gateway now owns routing, tracing, route rules, traffic budgets, logs and identity preparation. This PR locks the current Gateway contract before adding provider configuration and distributed traffic counters.

## Architecture and behavior impact

- Adds tests only.
- Does not change runtime behavior.
- Keeps backend APIs out of the route behavior tests by using unavailable local target URIs.
