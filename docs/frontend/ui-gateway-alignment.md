# UI Gateway alignment

## Purpose

React, Vue and Angular applications must call the platform in a consistent way.

This document aligns the frontend applications on the same Gateway behavior before real screens are added.

## Applications covered

```text
client-web-react
vue-app
angular-app
```

## Shared behavior

Every UI must follow the same integration behavior:

```text
- use a configurable Gateway base URL
- attach Authorization when an access value is available
- send X-Correlation-Id when a value is available
- read X-Correlation-Id from each response
- normalize API errors into the same frontend shape
- respect Retry-After for 429 responses
- avoid automatic retry for POST commands unless idempotency is explicit
```

## Common error shape

All apps should map failed Gateway calls to this conceptual shape:

```ts
export type UiApiError = {
  status: number;
  message: string;
  code?: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};
```

## Common response metadata

Successful calls may also expose response metadata:

```ts
export type UiApiMeta = {
  correlationId?: string;
};
```

## Header rules

| Header | Direction | Rule |
| --- | --- | --- |
| `Authorization` | request | Send `Bearer <access-value>` when the app has an access value. |
| `X-Correlation-Id` | request | Send when already known, otherwise the Gateway can create one. |
| `X-Correlation-Id` | response | Capture for support messages and diagnostics. |
| `Retry-After` | response | Convert to seconds when status is `429`. |

## Framework implementation strategy

| App | Implementation style |
| --- | --- |
| React | Gateway client plus React Query hooks. |
| Vue | Gateway client plus composables. |
| Angular | Gateway service plus HTTP interceptor. |

The UI framework can differ, but the request and error behavior must stay aligned.

## Status behavior

| Status | UI behavior |
| --- | --- |
| `400` | Display validation or request error. |
| `401` | Send the user to the sign-in flow. |
| `403` | Display access denied. |
| `404` | Display not found state. |
| `429` | Display temporary throttling and use `Retry-After`. |
| `5xx` | Display technical error with correlation id. |

## Shared package direction

The project can later add a framework-agnostic package:

```text
frontend/shared-contracts
```

It should contain only neutral TypeScript contracts and helpers:

```text
- API error type
- API metadata type
- endpoint constants
- header names
- status helpers
```

It should not contain React, Vue or Angular components.

## Rollout plan

```text
1. Keep React API client as the first concrete implementation.
2. Build Vue client wrapper using the same error and header rules.
3. Build Angular service and interceptor using the same rules.
4. Extract common framework-neutral contracts only when duplication becomes visible.
```

## Related documents

```text
docs/frontend/multi-app-architecture.md
docs/frontend/shared-frontend-rules.md
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
docs/frontend/ui-check-plan.md
```
