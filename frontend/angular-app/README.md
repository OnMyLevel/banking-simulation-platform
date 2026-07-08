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
- Angular HttpClient integration
- Gateway HTTP timeout and retry resilience
- configurable Gateway endpoint through Angular environment
- local development proxy for Gateway API calls
- Correlation ID HTTP interceptor
- Authentication HTTP interceptor
- app integration with Gateway-backed dashboard states
- component, app and service tests
```

## Commands

```text
npm install
npm run dev
npm run dev:proxy
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

## Gateway configuration

```text
src/environments/environment.ts
```

The Angular app reads Gateway routing values from the environment config:

```text
gatewayBaseUrl
advisorDashboardPath
```

The default setup keeps `gatewayBaseUrl` empty so local proxy or same-origin routing can serve `/api/advisor/dashboard`.

## Local Gateway proxy

```text
proxy.conf.json
npm run dev:proxy
```

The local proxy forwards Angular `/api` requests to the Gateway running on:

```text
http://localhost:8080
```

Use this command when running the Angular app locally with the Gateway service:

```text
npm run dev:proxy
```

## Gateway API service

```text
src/app/gateway-api.service.ts
```

The Gateway API service uses Angular `HttpClient` and exposes dashboard state as an RxJS observable. It converts HTTP responses into UI-ready dashboard states:

```text
- loading: Gateway data is being requested
- ready: dashboard data is available
- empty: no dashboard data is available
- error: Gateway error mapped through shared frontend error helpers
```

The service applies request resilience for Gateway calls:

```text
- timeout after 5000 ms
- retry 2 times for temporary failures only
- retryable failures: network errors, 502, 503, 504
- non-retryable failures: client and business errors such as 400, 401, 403, 404, 429
```

## Correlation ID interceptor

```text
src/app/correlation-id.interceptor.ts
```

The interceptor adds `X-Correlation-Id` to outgoing HTTP requests when the request does not already provide one. Existing correlation IDs are preserved.

## Authentication interceptor

```text
src/app/auth-token.service.ts
src/app/auth.interceptor.ts
```

The token service centralizes access to the current bearer token. The authentication interceptor adds `Authorization: Bearer <token>` to outgoing HTTP requests when a token is available. Existing authorization headers are preserved.

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
