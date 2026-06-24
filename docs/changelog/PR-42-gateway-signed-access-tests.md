# PR #42 - test Gateway signed access

## Files changed

- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayClaimAuthoritiesConverter.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/access/GatewayJwtAccessConfiguration.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/access/GatewayClaimAuthoritiesConverterTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewaySignedAccessRouteBehaviorTest.java`
- `docs/architecture/api-gateway-signed-access-tests.md`
- `backend/api-gateway/README.md`

## Features

- Signed access route tests with an in-memory RSA key pair.
- Role claim mapping from `roles` to `ROLE_*` authorities.
- Scope claim preservation as `SCOPE_*` authorities.
- Account route acceptance with `USER` role.
- Operation route acceptance with `ADVISOR` role.
- Operation route rejection with `OPS` role.
- Unauthorized behavior verified without bearer access.

## Reasons and goals

The Gateway had mock JWT route tests. This PR adds a stronger test level by creating signed bearer values locally and decoding them through the Gateway resource server path.

## Architecture and behavior impact

- Adds a converter for Gateway role claims.
- Wires the converter into the `jwt` profile.
- Keeps local default profile unchanged.
- Uses unavailable backend URLs to distinguish Gateway acceptance from backend availability.
