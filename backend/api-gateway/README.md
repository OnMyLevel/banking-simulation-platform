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

## Route rules

| Route | Rule |
| --- | --- |
| `/actuator/health` | public |
| `/actuator/info` | public |
| `/api/users/**` | public for MVP |
| `/api/accounts/**` | authenticated |
| `/api/operations/**` | authenticated |
| `/internal/**` | denied at Gateway |

Detailed route and traffic rules:

```text
docs/architecture/api-gateway-rules.md
```

## Identity preparation

The Gateway now contains the dependencies and constants needed for the future identity provider mode.

Target roles:

```text
USER
ADVISOR
ADMIN
OPS
```

The default local profile remains the MVP fallback. A dedicated profile contains the target route rules and can be completed once the identity provider configuration is available.

Detailed identity model:

```text
docs/architecture/gateway-identity-model.md
```

## Traffic budgets

Starting values:

```text
/api/users/**      120 requests/minute/client
/api/accounts/**    60 requests/minute/client
/api/operations/**  30 requests/minute/client
default            180 requests/minute/client
```

A caller over budget receives:

```http
HTTP/1.1 429 Too Many Requests
Retry-After: 60
```

## Request logs

The Gateway logs technical metadata for each request:

```text
gateway_request method=<HTTP_METHOD> path=<REQUEST_PATH> status=<HTTP_STATUS> durationMs=<DURATION_MS> correlationId=<X_CORRELATION_ID>
```

Sensitive values are intentionally excluded: request body, response body, authorization headers, cookies and business data.

Detailed logging guide:

```text
docs/architecture/api-gateway-logs.md
```

## Route behavior tests

The Gateway has HTTP-level route behavior tests covering public health, denied internal paths, MVP public user paths, protected account and operation paths, and correlation id response behavior.

Detailed test guide:

```text
docs/architecture/api-gateway-tests.md
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
- identity provider dependency prepared;
- route configuration for User, Account and Core APIs;
- request trace header filter;
- route access rules;
- target identity role constants;
- per-client traffic budget filter;
- technical request logging filter;
- route behavior tests;
- Actuator health and info endpoints;
- Dockerfile.

## Next steps

- complete identity provider environment configuration;
- add signed-token integration tests;
- replace in-memory traffic budgets with Redis-backed counters.
