# API Gateway

Spring Cloud Gateway entry point for the Banking Simulation Platform.

## Responsibilities

- expose one HTTP entry point for frontend applications;
- route public API traffic to backend services;
- keep backend service URLs hidden from clients;
- propagate request trace headers for distributed troubleshooting;
- prepare cross-cutting concerns such as authentication, rate limiting and request logging.

## Local port

```text
8080
```

## Routes

| Public path | Target service | Target URI |
| --- | --- | --- |
| `/api/users/**` | User Management API | `BANKING_USER_API_URI` |
| `/api/accounts/**` | Account Banking API | `BANKING_ACCOUNT_API_URI` |
| `/api/operations/**` | Core Banking API | `BANKING_CORE_API_URI` |

Default local targets:

```text
BANKING_USER_API_URI=http://localhost:8081
BANKING_ACCOUNT_API_URI=http://localhost:8082
BANKING_CORE_API_URI=http://localhost:8083
```

Docker targets:

```text
BANKING_USER_API_URI=http://user-management-api:8081
BANKING_ACCOUNT_API_URI=http://account-banking-api:8082
BANKING_CORE_API_URI=http://core-banking-api:8083
```

## Request trace header

The Gateway uses this header:

```text
X-Correlation-Id
```

Behavior:

```text
- if the request already contains X-Correlation-Id, the Gateway keeps it;
- if the request does not contain it, the Gateway generates a UUID;
- the Gateway forwards the value to the backend API;
- the Gateway returns the value in the HTTP response.
```

Detailed algorithm:

```text
docs/architecture/api-gateway-flow.md
```

## Health endpoints

```http
GET /actuator/health
GET /actuator/info
```

## Route examples

```http
GET /api/accounts/accounts/{accountId}
POST /api/operations/operations/credits
POST /api/operations/operations/debits
POST /api/operations/operations/transfers
```

The current routing keeps backend endpoint paths unchanged after the service prefix is removed. A later PR can refine external API naming once the frontend contract is defined.

## Current status

Implemented foundation:

- Spring Boot application;
- Spring Cloud Gateway dependency;
- route configuration for User, Account and Core APIs;
- request trace header filter;
- Actuator health and info endpoints;
- Dockerfile.

## Next steps

- add gateway-level request logging;
- add JWT/OAuth2 validation;
- add rate limiting;
- add gateway tests for route behavior.
