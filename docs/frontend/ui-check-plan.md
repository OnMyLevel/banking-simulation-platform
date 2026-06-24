# UI check plan

## Purpose

The platform has three UI applications:

```text
client-web-react
business-web-vue
advisor-admin-angular
```

Each UI must have the same quality expectations before it is merged or deployed.

This document defines the validation plan for UI code.

## Applications covered

| Application | Framework | Status |
| --- | --- | --- |
| `client-web-react` | React + TypeScript + Vite | setup started |
| `business-web-vue` | Vue + TypeScript + Vite | planned |
| `advisor-admin-angular` | Angular + TypeScript | planned |

## Required checks per UI

Each UI must support these commands:

```text
npm install
npm run lint
npm run test
npm run build
```

When an app adds browser journey checks, it should also support:

```text
npm run test:e2e
```

## Quality levels

### 1. Static checks

Goal: catch syntax, typing and style problems early.

Expected checks:

```text
- TypeScript compilation
- ESLint
- framework-specific lint rules
- no unused critical code
```

### 2. Unit tests

Goal: verify small isolated behavior.

Examples:

```text
- API error normalization
- Gateway client request building
- components with simple display rules
- form validation helpers
- utility functions
```

Recommended tools:

| UI | Tooling |
| --- | --- |
| React | Vitest + Testing Library |
| Vue | Vitest + Vue Testing Library or Vue Test Utils |
| Angular | Angular TestBed + Jest or Karma depending setup |

### 3. Integration checks

Goal: verify UI modules working together with mocked Gateway responses.

Examples:

```text
- account page loads account data from mocked Gateway
- operation form submits payload to mocked Gateway
- 401 redirects to sign-in flow
- 403 displays access denied
- 429 displays temporary throttling message
- 5xx displays technical error with correlation id
```

Gateway must be mocked at the HTTP boundary. Frontend checks must not call backend services directly.

### 4. Browser journey checks

Goal: verify key user journeys in a real browser.

Recommended tool:

```text
Playwright
```

First journeys:

```text
- app shell loads
- account page happy path
- operation form validation
- 401 handling
- 429 handling
```

### 5. Load-oriented checks

UI load work should be split into two parts:

```text
- browser journey stability with Playwright
- API load against API Gateway with k6 or equivalent
```

The UI itself should not be the main load target. The main load target is the API Gateway and backend APIs.

Frontend load checks should focus on:

```text
- page boot time
- repeated navigation stability
- memory leak detection where possible
- API Gateway behavior under concurrent clients
```

### 6. Frontend safety checks

Goal: reduce common frontend risks.

Expected checks:

```text
- dependency vulnerability scan
- secret scan in repository and pipeline logs
- no bearer value logged in browser console
- no raw stack trace shown to users
- no sensitive data in technical logs
- headers handled consistently
```

## GitHub Actions strategy

Recommended workflow split:

```text
.github/workflows/frontend-client-web.yml
.github/workflows/frontend-business-web.yml
.github/workflows/frontend-advisor-admin.yml
.github/workflows/frontend-e2e.yml
.github/workflows/frontend-safety.yml
```

Start small, then expand.

### First workflow target

The first practical pipeline workflow should target `client-web-react` because that app already has a project setup.

Required jobs:

```text
- install dependencies
- lint
- unit checks
- build
```

### Later workflow targets

After Vue and Angular setup PRs:

```text
- add Vue lint/test/build job
- add Angular lint/test/build job
- add shared browser journey workflow
- add periodic API load workflow against local stack
```

## Quality gates before merge

A frontend PR should not be merged if:

```text
- lint fails
- unit checks fail
- build fails
- Gateway contract is broken
- error handling loses correlation id
- protected routes ignore 401 or 403 behavior
```

For later mature stages, PRs should also block on:

```text
- critical dependency issue
- browser journey failure on main path
- bundle size regression beyond threshold
```

## Related documents

```text
docs/frontend/multi-app-architecture.md
docs/frontend/shared-frontend-rules.md
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
docs/architecture/api-gateway-public-contract.md
```
