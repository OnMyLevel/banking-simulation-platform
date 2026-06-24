# PR #46 - test Gateway forwarding

## Files changed

- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewayRouteForwardingTest.java`
- `docs/architecture/api-gateway-forwarding-tests.md`
- `docs/architecture/api-gateway-public-contract.md`

## Features

- Gateway forwarding tests with in-memory backend stubs.
- Dynamic Gateway target URI configuration in tests.
- User route forwarding verification.
- Account route forwarding verification for the clean public account path.
- Operation route forwarding verification for the clean public operation path.
- Public contract updated with forwarding test expectations.

## Checked mappings

```text
GET /api/users/profile -> /profile
GET /api/accounts/{accountId} -> /accounts/{accountId}
POST /api/operations/credits -> /operations/credits
```

## Reasons and goals

PR #45 documented the clean public route contract. This PR verifies that accepted Gateway requests reach the expected backend paths.

## Architecture and behavior impact

- Adds test coverage only.
- Does not change runtime route configuration.
- Protects frontend route contract and backend compatibility.
