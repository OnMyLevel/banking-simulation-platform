# Shared frontend rules

## Purpose

The platform has several frontend applications. This document defines rules that must stay consistent across React, Vue and Angular applications.

## API access

```text
- Frontends call the API Gateway only.
- Frontends do not call backend services directly.
- Public routes come from the Gateway public contract.
```

## Headers

Every frontend must support:

```http
Authorization: Bearer <access-value>
X-Correlation-Id: <trace-id>
```

Rules:

```text
- add Authorization when an access value exists
- send X-Correlation-Id when the UI already has one
- read X-Correlation-Id from every response
- keep X-Correlation-Id for support and diagnostics
```

## Error handling

Every frontend should normalize errors into a similar shape:

```ts
export type ApiError = {
  status: number;
  message: string;
  code?: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};
```

## Status handling

| Status | Shared behavior |
| --- | --- |
| `400` | Show validation or request error. |
| `401` | Ask user to sign in again. |
| `403` | Show access denied. |
| `404` | Show not found state. |
| `429` | Respect `Retry-After` before retrying. |
| `5xx` | Show technical error with correlation id. |

## Retry rules

```text
- do not retry 400, 401 or 403 automatically
- do not aggressively retry 429
- wait at least Retry-After seconds for 429
- retry GET calls carefully on 5xx when useful
- do not retry POST operations automatically without idempotency
```

## State management

Each frontend can use the state tool that fits its framework:

```text
React   -> React Query plus local component state
Vue     -> Pinia or Vue Query when needed
Angular -> services plus RxJS streams or signals
```

The API contract must remain independent from UI state choices.

## UI consistency

Screens do not need to share components across frameworks. They should share behavior and vocabulary:

```text
- same route naming
- same error handling rules
- same loading and empty-state behavior principles
- same correlation id support
```

## Security and privacy

```text
- never display raw bearer values
- never log access values in the browser console
- never expose backend stack traces to users
- keep sensitive business data out of technical logs
```
