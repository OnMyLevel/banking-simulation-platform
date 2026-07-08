# Angular App

Angular frontend application for support and operational journeys.

## Current scope

```text
- Angular setup
- TypeScript setup
- advisor dashboard shell
- ready, empty and error display states
- shared error mapping
- Gateway API service for advisor dashboard data
- component and service tests
```

## Commands

```text
npm install
npm run dev
npm run lint
npm run test
npm run build
```

## Main screen

```text
src/app/advisor-dashboard.component.ts
```

The dashboard prepares the advisor workspace for Gateway-backed data. It currently renders state through component inputs.

## Gateway API service

```text
src/app/gateway-api.service.ts
```

The Gateway API service prepares the Angular app for advisor dashboard data loading. It converts HTTP responses into UI-ready dashboard states:

```text
- ready: dashboard data is available
- empty: no dashboard data is available
- error: Gateway error mapped through shared frontend error helpers
```

## Shared types usage

```text
../common-types/src
src/app/gateway-error.mapper.ts
src/app/gateway-api.service.ts
```

## Related documents

```text
docs/frontend/multi-app-architecture.md
docs/frontend/shared-frontend-rules.md
docs/frontend/ui-gateway-alignment.md
docs/frontend/ui-check-plan.md
```
