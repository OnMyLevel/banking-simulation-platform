# Frontend multi-app architecture

## Purpose

The platform targets three frontend applications. Each UI serves a different audience, but all of them must consume the platform through the API Gateway.

This document defines the multi-app frontend architecture before adding the Vue and Angular applications.

## Frontend applications

| Application | Technology | Target users | Main purpose |
| --- | --- | --- | --- |
| `client-web-react` | React + TypeScript + Vite | Individual banking clients | Account consultation and personal operations. |
| `business-web-vue` | Vue + TypeScript + Vite | Business banking clients | Business accounts, business payments and company users. |
| `advisor-admin-angular` | Angular + TypeScript | Advisors and administrators | Support, supervision and administrative actions. |

## High-level flow

```text
client-web-react
business-web-vue       -> API Gateway -> backend services
advisor-admin-angular
```

Frontend applications must not call backend services directly.

## Shared frontend principles

All frontend applications must follow the same cross-cutting rules:

```text
- call only the API Gateway
- attach Authorization when bearer access is available
- propagate or capture X-Correlation-Id
- normalize API errors consistently
- respect Retry-After on 429 responses
- avoid automatic retry of non-idempotent POST operations
- keep business rules in backend services
- keep UI state and display logic in frontend applications
```

## Shared Gateway contract

The shared contract is documented in:

```text
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
docs/frontend/ui-gateway-alignment.md
docs/architecture/api-gateway-public-contract.md
```

Every frontend must implement the same conceptual Gateway client behavior:

```text
- base URL configuration
- bearer access injection
- correlation id support
- JSON parsing
- error normalization
- Retry-After handling
```

## Frontend-specific responsibilities

### client-web-react

Owns individual customer journeys:

```text
- view personal accounts
- consult balances
- create personal credit/debit operations
- create transfers
- view personal operation history
```

### business-web-vue

Owns business banking journeys:

```text
- view company accounts
- prepare business payments
- manage company users when supported
- monitor business operation history
```

### advisor-admin-angular

Owns support and administrative journeys:

```text
- search customer or business data when authorized
- support account and operation investigations
- view technical or operational states
- perform administrative actions when supported
```

## Code sharing strategy

The first implementation can keep each application independent while sharing documentation and contracts.

Recommended future direction:

```text
frontend/
  client-web-react/
  business-web-vue/
  advisor-admin-angular/
  shared-contracts/
```

`shared-contracts` can later contain framework-agnostic TypeScript types, endpoint constants and error models. UI components should not be shared across React, Vue and Angular.

## API client strategy

Each app can have its own framework-specific wrapper, but the contract must stay aligned:

```text
React   -> GatewayClient + React Query hooks
Vue     -> Gateway client + composables
Angular -> Gateway service + HttpInterceptor
```

Common behavior that must stay aligned:

```text
- Authorization header
- X-Correlation-Id header
- 401 handling
- 403 handling
- 429 Retry-After handling
- 5xx technical error handling
```

## UI check strategy

All UI applications must follow the shared check plan:

```text
docs/frontend/ui-check-plan.md
docs/frontend/ui-pipeline-roadmap.md
```

Baseline checks for each app:

```text
npm install
npm run lint
npm run test
npm run build
```

## Observability rules

Frontend error screens and logs should preserve the correlation id returned by the Gateway.

For support scenarios, UI error messages should include a short reference value based on `X-Correlation-Id`, not backend stack traces.

## Setup order

Recommended order:

```text
1. client-web-react setup
2. multi-app architecture documentation
3. UI check plan
4. business-web-vue setup
5. advisor-admin-angular setup
6. Gateway client alignment
7. shared contracts package when duplication appears
```

## Related documents

```text
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
docs/frontend/ui-gateway-alignment.md
docs/frontend/ui-check-plan.md
docs/frontend/ui-pipeline-roadmap.md
docs/architecture/api-gateway-public-contract.md
docs/architecture/api-gateway-forwarding-tests.md
backend/api-gateway/README.md
```
