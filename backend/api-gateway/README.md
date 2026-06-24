# API Gateway

Spring Cloud Gateway entry point for the Banking Simulation Platform.

## Responsibilities

- expose one HTTP entry point for frontend applications;
- route public API traffic to backend services;
- keep backend service URLs hidden from clients;
- propagate request trace headers for distributed troubleshooting;
- apply route access rules;
- apply simple per-client traffic budgets;
- log technical request metadata without sensitive data;
- expose Gateway telemetry;
- prepare cross-cutting concerns such as identity validation.

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

## Public contract

The frontend-facing route contract uses clean route names:

```http
GET /api/accounts/{accountId}
POST /api/operations/credits
POST /api/operations/debits
POST /api/operations/transfers
```

Detailed public contract:

```text
docs/architecture/api-gateway-public-contract.md
```

## Route rules

| Route | Rule |
| --- | --- |
| `/actuator/health` | public |
| `/actuator/info` | public |
| `/api/users/**` | public for MVP |
| `/api/accounts/**` | authenticated |
| `/api/operations/**` | authenticated |
| `/internal/**` | denied at Gateway |

## Route examples

```http
GET /api/accounts/{accountId}
POST /api/operations/credits
POST /api/operations/debits
POST /api/operations/transfers
```

The public Gateway contract avoids duplicated path segments and hides backend implementation details.

## Related docs

```text
docs/architecture/api-gateway-flow.md
docs/architecture/api-gateway-rules.md
docs/architecture/api-gateway-logs.md
docs/architecture/api-gateway-telemetry.md
docs/architecture/api-gateway-tests.md
docs/architecture/api-gateway-jwt-tests.md
docs/architecture/api-gateway-signed-access-tests.md
docs/observability/gateway-ops-guide.md
```

## Current status

Implemented foundation:

- Spring Boot application;
- Spring Cloud Gateway dependency;
- identity provider dependency prepared;
- identity provider settings documented;
- clean public API contract documented;
- route configuration for User, Account and Core APIs;
- request trace header filter;
- route access rules;
- target identity role constants;
- per-client traffic budget filter;
- Gateway telemetry;
- Gateway operations guide;
- technical request logging filter;
- route behavior tests;
- jwt profile route tests;
- signed access tests;
- Actuator health and info endpoints;
- Dockerfile.

## Next steps

- add frontend integration documentation;
- add backend stub route forwarding tests.
