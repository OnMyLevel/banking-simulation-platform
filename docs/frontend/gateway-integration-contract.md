# Frontend Gateway integration contract

## Purpose

The frontend must consume the platform through the API Gateway, not through backend services directly.

This document defines the first integration contract between the frontend and the Gateway.

## Base URL

Local development:

```text
http://localhost:8080
```

Docker development:

```text
http://api-gateway:8080
```

Browser applications should use the public Gateway URL configured for the target environment.

## Required headers

### Authorization

Protected routes require bearer access:

```http
Authorization: Bearer <access-value>
```

Public MVP routes under `/api/users/**` may remain available without bearer access until the identity flow is finalized.

### Correlation id

The frontend may send:

```http
X-Correlation-Id: <uuid-or-client-trace-id>
```

If omitted, the Gateway generates one and returns it in the response.

Frontend clients should store the returned value when displaying or reporting technical errors.

## Public endpoints

### Accounts

| Method | Path | Purpose | Access |
| --- | --- | --- | --- |
| `GET` | `/api/accounts/{accountId}` | Read one account. | Bearer required. |
| `GET` | `/api/accounts` | List accounts when supported. | Bearer required. |

### Operations

| Method | Path | Purpose | Access |
| --- | --- | --- | --- |
| `POST` | `/api/operations/credits` | Create a credit operation. | Bearer required. |
| `POST` | `/api/operations/debits` | Create a debit operation. | Bearer required. |
| `POST` | `/api/operations/transfers` | Create a transfer operation. | Bearer required. |
| `GET` | `/api/operations/history` | Read operation history when supported. | Bearer required. |

### Users

| Method | Path | Purpose | Access |
| --- | --- | --- | --- |
| `GET` | `/api/users/**` | User-facing MVP routes. | Public for MVP. |

## Example requests

### Read account

```http
GET /api/accounts/123
Authorization: Bearer <access-value>
X-Correlation-Id: 3f3e8a2a-84e1-4c89-8bb6-0c7761f0c001
```

### Create credit

```http
POST /api/operations/credits
Authorization: Bearer <access-value>
Content-Type: application/json
X-Correlation-Id: 3f3e8a2a-84e1-4c89-8bb6-0c7761f0c001
```

## Expected status codes

| Status | Meaning | Frontend behavior |
| --- | --- | --- |
| `200` / `201` | Request succeeded. | Continue normal flow. |
| `400` | Invalid request payload or parameters. | Display validation message when available. |
| `401` | Missing or invalid bearer access. | Redirect to login or refresh identity flow. |
| `403` | Caller is known but not allowed. | Show access denied. |
| `404` | Resource not found. | Show not found state. |
| `429` | Traffic budget exceeded. | Respect `Retry-After` header and slow retries. |
| `5xx` | Gateway or backend failure. | Show technical error and keep correlation id. |

## Error handling rules

Frontend clients should normalize errors into this shape:

```ts
export type ApiError = {
  status: number;
  code?: string;
  message: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};
```

Rules:

```text
- always read X-Correlation-Id from the response
- if status is 429, read Retry-After
- never expose raw technical stack traces to users
- log technical details in the browser console only for local development
- show a user-friendly message in the UI
```

## Suggested API client structure

```text
src/
  api/
    gatewayClient.ts
    accountsApi.ts
    operationsApi.ts
    usersApi.ts
    apiErrors.ts
```

### Gateway client responsibilities

```text
- hold base URL configuration
- attach Authorization when available
- attach X-Correlation-Id when provided by the caller
- parse JSON responses
- normalize API errors
- expose Retry-After for 429 responses
```

### Domain API files

```text
accountsApi.ts    -> account-related calls
operationsApi.ts  -> credit, debit, transfer and history calls
usersApi.ts       -> user-facing calls
```

## Retry guidance

```text
- do not automatically retry 400, 401 or 403
- do not aggressively retry 429
- for 429, wait at least Retry-After seconds
- for 5xx, allow limited retry only for idempotent GET calls
- do not retry POST operations automatically without an idempotency strategy
```

## Related backend documents

```text
docs/architecture/api-gateway-public-contract.md
docs/architecture/api-gateway-forwarding-tests.md
docs/architecture/api-gateway-telemetry.md
docs/observability/gateway-ops-guide.md
```
