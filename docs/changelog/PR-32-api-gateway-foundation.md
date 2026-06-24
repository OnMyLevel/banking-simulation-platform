# PR #32 - feat API Gateway foundation

## Files changed

- `backend/api-gateway/pom.xml`
- `backend/api-gateway/src/main/java/com/banking/gateway/ApiGatewayApplication.java`
- `backend/api-gateway/src/main/resources/application.yml`
- `backend/api-gateway/src/test/java/com/banking/gateway/ApiGatewayApplicationTest.java`
- `backend/api-gateway/Dockerfile`
- `backend/api-gateway/README.md`
- `docker-compose.yml`
- `README.md`

## Concepts and features

- Spring Cloud Gateway module.
- Single HTTP entry point on port `8080`.
- Route to User Management API.
- Route to Account Banking API.
- Route to Core Banking API.
- Actuator health and info endpoints.
- Docker Compose service definition.

## Routes added

```text
/api/users/**      -> user-management-api
/api/accounts/**   -> account-banking-api
/api/operations/** -> core-banking-api
```

## Reasons and goals

The platform now has several backend services. This PR introduces the gateway foundation so frontend applications can use one entry point instead of calling each service directly.

## Architecture and behavior impact

- Adds a new backend service: `api-gateway`.
- Does not change existing service behavior.
- Prepares future cross-cutting concerns: JWT validation, correlation id propagation, rate limiting and request logging.
