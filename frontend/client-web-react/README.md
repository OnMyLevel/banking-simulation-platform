# Client Web React

Client banking portal.

Technology target:

- React
- TypeScript
- Vite
- React Query
- Tailwind CSS

## API client foundation

The frontend API layer starts in:

```text
src/api/
```

Current modules:

```text
apiErrors.ts
GatewayClient.ts equivalent: gatewayClient.ts
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
```

## Current status

The API client foundation is framework-light and can be reused once the React/Vite setup is added.
