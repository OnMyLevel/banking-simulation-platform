# PR #34 - feat Gateway traffic rules

## Files changed

- `backend/api-gateway/pom.xml`
- `backend/api-gateway/src/main/resources/application.yml`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayRouteRulesConfiguration.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/GatewayTrafficProperties.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficBudgetFilter.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/traffic/TrafficBudgetFilterTest.java`
- `docs/architecture/api-gateway-rules.md`
- `backend/api-gateway/README.md`

## Concepts and features

- Gateway route access matrix.
- Public health and info endpoints.
- Public user route for MVP.
- Authenticated account and operation routes.
- Denied internal routes at the Gateway boundary.
- In-memory per-client traffic budget by route family.
- HTTP 429 response with `Retry-After` header when the budget is exceeded.
- Documentation of route rules and traffic budget algorithm.

## Initial traffic budgets

```text
/api/users/**      120 requests/minute/client
/api/accounts/**    60 requests/minute/client
/api/operations/**  30 requests/minute/client
default            180 requests/minute/client
```

## Reasons and goals

The API Gateway is now the public entry point. This PR adds basic route protection and request volume control before adding JWT/OAuth2 and distributed counters.

## Architecture and behavior impact

- Adds Spring Security WebFlux support to the Gateway.
- Adds a lightweight in-memory traffic budget filter.
- Keeps business authorization inside owning backend services.
- Prepares later migration to JWT/OAuth2 and Redis-backed traffic control.
