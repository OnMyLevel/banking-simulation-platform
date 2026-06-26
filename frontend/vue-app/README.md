# Vue App

Vue frontend application for company journeys.

## Current scope

```text
- Vue setup
- TypeScript setup
- company dashboard shell
- ready, empty and error display states
- shared error mapping
- component tests
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
src/features/company/CompanyDashboard.vue
```

The dashboard prepares the company portal for future Gateway-backed data. It currently renders static state through component props.

## Shared types usage

```text
../common-types/src
src/composables/useGatewayError.ts
```

## Related documents

```text
docs/frontend/multi-app-architecture.md
docs/frontend/ui-gateway-alignment.md
docs/frontend/ui-check-plan.md
```
