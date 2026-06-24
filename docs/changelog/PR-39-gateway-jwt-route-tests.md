# PR #39 - test Gateway JWT route rules

## Files changed

- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewayJwtRouteBehaviorTest.java`
- `docs/architecture/api-gateway-jwt-tests.md`
- `backend/api-gateway/README.md`

## Concepts and features

- HTTP-level route tests for the `jwt` profile.
- Public health endpoint verification in `jwt` profile.
- Account route rejection without caller identity.
- Account route access with `USER` role.
- Operation route access with `ADVISOR` role.
- Operation route rejection with `OPS` role.
- Internal route rejection even with `ADMIN` role.
- Documentation of JWT route test strategy.

## Tested scenarios

```text
GET /actuator/health without caller       -> 200 OK
GET /api/accounts/** without caller       -> 401 Unauthorized
GET /api/accounts/** as USER              -> allowed through Gateway
POST /api/operations/** as ADVISOR        -> allowed through Gateway
POST /api/operations/** as OPS            -> 403 Forbidden
GET /internal/** as ADMIN                 -> 403 Forbidden
```

## Reasons and goals

The Gateway has a prepared identity profile. This PR locks the expected route rules before adding real signed-token integration tests.

## Architecture and behavior impact

- Adds tests only.
- Does not change runtime behavior.
- Uses Spring Security test support to create in-memory JWT callers.
