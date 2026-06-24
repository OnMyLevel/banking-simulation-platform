# PR #38 - feat Gateway provider config

## Files changed

- `backend/api-gateway/src/main/resources/application.yml`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayProviderProperties.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayJwtAccessConfiguration.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/access/GatewayProviderPropertiesTest.java`
- `docs/architecture/gateway-identity-model.md`
- `backend/api-gateway/README.md`

## Concepts and features

- Gateway provider settings under `banking.gateway.provider`.
- Environment variable mapping for provider name, issuer, keys path and audience.
- Provider properties class with defaults.
- Unit tests for provider properties.
- Identity model documentation updated.
- Gateway README updated.

## Environment variables

```text
BANKING_GATEWAY_PROVIDER_NAME
BANKING_GATEWAY_PROVIDER_ISSUER
BANKING_GATEWAY_PROVIDER_KEYS_PATH
BANKING_GATEWAY_PROVIDER_AUDIENCE
```

## Reasons and goals

PR #36 prepared the Gateway identity mode. This PR makes the expected provider configuration explicit and testable without forcing a concrete provider into local development.

## Architecture and behavior impact

- No change to default local behavior.
- Adds documented configuration points for future provider integration.
- Keeps signed token tests for a later PR.
