# PR #24 - feat security preparation

## Files changed

- `backend/core-banking-api/pom.xml`
- `backend/core-banking-api/src/main/java/com/banking/core/config/SecurityRoles.java`
- `backend/core-banking-api/src/main/java/com/banking/core/config/InternalSecurityConfiguration.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- OAuth2 Resource Server dependency added.
- Security roles centralized in `SecurityRoles`.
- Local Basic Auth configuration explicitly scoped away from the future token profile.
- Internal endpoint protection remains unchanged: `OPS` or `ADMIN` role is required.
- Documentation now describes the target centralized identity migration.

## Reasons and goals

The previous PR protected internal endpoints with temporary in-memory users. This PR prepares the codebase for a future token-based resource server without changing MVP runtime behavior yet.

## Architecture and behavior impact

- Runtime behavior stays compatible with the local MVP setup.
- Security role names are now centralized.
- The Core API can now evolve toward a centralized identity provider in a dedicated follow-up PR.
