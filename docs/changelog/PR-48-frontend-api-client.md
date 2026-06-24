# PR #48 - Frontend API client foundation

## Files changed

- `frontend/client-web-react/src/api/apiErrors.ts`
- `frontend/client-web-react/src/api/gatewayClient.ts`
- `frontend/client-web-react/src/api/accountsApi.ts`
- `frontend/client-web-react/src/api/operationsApi.ts`
- `frontend/client-web-react/src/api/profileApi.ts`
- `frontend/client-web-react/src/api/index.ts`
- `frontend/client-web-react/src/api/gatewayClient.test.ts`
- `frontend/client-web-react/README.md`

## Features

- Gateway client foundation.
- API error normalization.
- Authorization header injection.
- Correlation id propagation.
- Retry-After parsing for 429 responses.
- Account API wrapper.
- Operation API wrapper.
- Profile API wrapper.
- Initial Gateway client tests.
- Frontend README updated.

## Reasons and goals

PR #47 defined the frontend integration contract. This PR starts the frontend API layer so future UI code calls the Gateway consistently.

## Architecture and behavior impact

- Adds frontend API client code only.
- No backend runtime behavior change.
- Keeps the API layer framework-light before the React/Vite setup is completed.
