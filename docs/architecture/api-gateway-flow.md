# API Gateway flow

## Purpose

The API Gateway is the single entry point for frontend applications. It hides backend service URLs and prepares shared platform concerns before a request reaches a business API.

## High-level flow

```text
Client
  -> API Gateway
      -> User Management API
      -> Account Banking API
      -> Core Banking API
```

## Request algorithm

```text
1. Receive the client HTTP request.
2. Read X-Correlation-Id from the request headers.
3. If X-Correlation-Id is missing or blank, generate a new UUID.
4. Replace or add X-Correlation-Id on the request forwarded downstream.
5. Match the request path against configured Gateway routes.
6. Remove the external Gateway prefix according to the route filter.
7. Forward the request to the selected backend API.
8. Add the same X-Correlation-Id to the Gateway response.
9. Return the backend response to the client.
```

## Correlation id rule

Header name:

```text
X-Correlation-Id
```

Rules:

```text
- Preserve the incoming value if it is present and not blank.
- Generate a UUID if the header is missing or blank.
- Propagate the value to the selected backend API.
- Return the same value to the client in the response.
```

## Route matching

Current routes:

```text
/api/users/**      -> user-management-api
/api/accounts/**   -> account-banking-api
/api/operations/** -> core-banking-api
```

Current path rewrite rule:

```text
/api/users/**      -> StripPrefix=2
/api/accounts/**   -> StripPrefix=1
/api/operations/** -> StripPrefix=1
```

## Example

Input request:

```http
POST /api/operations/operations/credits
X-Correlation-Id: demo-123
```

Gateway processing:

```text
- preserves demo-123
- matches /api/operations/**
- forwards to Core Banking API
- returns X-Correlation-Id: demo-123 in the response
```

## Future shared concerns

The Gateway is the right location for cross-cutting entry-point behavior:

```text
- JWT/OAuth2 validation
- rate limiting
- correlation id propagation
- request logging
- response headers
- public/internal route separation
```

These concerns should remain thin at Gateway level. Business rules stay inside their owning backend service.
