# API Gateway forwarding tests

## Purpose

The public Gateway contract exposes clean frontend-facing routes. The Gateway must still send each request to the backend path expected by the target service.

This guide documents the forwarding test strategy.

## Tested routes

| Public route | Expected backend path |
| --- | --- |
| `GET /api/users/profile` | `/profile` |
| `GET /api/accounts/{accountId}` | `/accounts/{accountId}` |
| `POST /api/operations/credits` | `/operations/credits` |

## Test design

The test suite starts lightweight in-memory HTTP backends using the JDK HTTP server.

```text
1. Start one stub backend per route family.
2. Configure Gateway target URIs dynamically.
3. Send a request through the Gateway public route.
4. Capture the path received by the stub backend.
5. Assert that the received path matches the backend contract.
```

## Why this matters

Route behavior tests prove whether the Gateway accepts or rejects a caller. Forwarding tests prove where accepted requests are sent after route filters are applied.

Together, these tests protect the public API contract and backend compatibility.

## Notes

The forwarding tests run with the `jwt` profile for protected account and operation routes. `mockJwt` supplies the allowed role, while the test focuses on path forwarding.
