# Angular App

Angular frontend application for support and operational journeys.

## Current scope

```text
- Angular setup
- TypeScript setup
- advisor dashboard shell
- ready, empty, loading and error display states
- shared error mapping
- Gateway API service for advisor dashboard data
- app integration with Gateway-backed dashboard states
- component, app and service tests
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
src/app/app.component.ts
src/app/advisor-dashboard.component.ts
```

The app root loads advisor dashboard state through the Gateway API service and passes UI-ready state into the dashboard component.

## Gateway API service

```text
src/app/gateway-api.service.ts
```

The Gateway API service prepares the Angular app for advisor dashboard data loading. It converts HTTP responses into UI-ready dashboard states:

```text
- loading: Gateway data is being requested
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
