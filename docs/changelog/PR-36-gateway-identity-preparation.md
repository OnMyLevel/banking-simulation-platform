# PR #36 - feat Gateway identity preparation

## Files changed

- `backend/api-gateway/pom.xml`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayRouteRulesConfiguration.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayRoles.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayJwtAccessConfiguration.java`
- `docs/architecture/gateway-identity-model.md`
- `backend/api-gateway/README.md`

## Concepts and features

- Resource server dependency added to the Gateway.
- Default local profile kept as MVP fallback.
- Dedicated `jwt` profile prepared for token-based route rules.
- Gateway role constants added.
- Target roles documented: `USER`, `ADVISOR`, `ADMIN`, `OPS`.
- Identity model documentation added.

## Reasons and goals

The Gateway already handles routing, correlation id, traffic rules and technical logs. This PR prepares the next step: centralized identity validation at the Gateway without breaking local development.

## Architecture and behavior impact

- Local behavior remains unchanged outside the `jwt` profile.
- Token-based route rules are prepared behind the `jwt` profile.
- The actual identity provider URL and signed-token integration tests will be added in a later PR.
