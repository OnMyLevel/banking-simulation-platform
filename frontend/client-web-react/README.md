# Client Web React

Client banking portal.

Technology target:

- React
- TypeScript
- Vite
- React Query
- Tailwind CSS

## Setup

From this folder:

```bash
npm install
npm run dev
npm run build
npm run test
npm run lint
```

## API client foundation

The frontend API layer starts in:

```text
src/api/
```

Current modules:

```text
apiErrors.ts
gatewayClient.ts
accountsApi.ts
operationsApi.ts
profileApi.ts
index.ts
```

Responsibilities:

```text
- call the API Gateway, not backend services directly
- attach Authorization when an access value is available
- propagate X-Correlation-Id
- parse JSON responses
- normalize API errors
- expose Retry-After for 429 responses
```

Gateway contract:

```text
docs/frontend/gateway-integration-contract.md
docs/frontend/gateway-client-outline.md
docs/frontend/ui-gateway-alignment.md
```

## Account overview

The first screen is implemented in:

```text
src/features/accounts/AccountOverview.tsx
```

It loads account data through `accountsApi`, handles loading, success and error states, and displays the correlation id when available.

## Current status

The React and Vite foundation is in place with TypeScript, Vitest, ESLint configuration and the first account overview screen.
