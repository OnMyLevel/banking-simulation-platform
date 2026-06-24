# PR #23 - feat internal security foundation

## Files changed

- `backend/core-banking-api/pom.xml`
- `backend/core-banking-api/src/main/java/com/banking/core/config/InternalSecurityConfiguration.java`
- `backend/core-banking-api/src/test/java/com/banking/core/api/controller/OutboxInternalControllerSecurityTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Spring Security foundation for Core Banking API.
- `/internal/**` protected by `OPS` or `ADMIN` role.
- Public endpoints remain temporarily accessible for the MVP.
- Stateless HTTP Basic authentication for local testing.
- Temporary in-memory users for local MVP testing.
- Security tests for internal outbox endpoints.

## Reasons and goals

PR #22 introduced internal operation endpoints for outbox recovery. These endpoints should not stay open. This PR adds the first security layer while keeping the design ready for OAuth2/JWT later.

## Architecture and behavior impact

- Internal endpoints now require authentication and authorization.
- Other endpoints stay open for the current MVP.
- Temporary users must be replaced by centralized identity in a later PR.
