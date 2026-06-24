# PR #47 - Frontend Gateway integration contract

## Files changed

- `docs/frontend/gateway-integration-contract.md`
- `docs/frontend/gateway-client-outline.md`
- `README.md`

## Features

- Frontend-to-Gateway integration contract.
- Public Gateway base URL guidance.
- Required headers documented.
- Gateway endpoints grouped by domain.
- Expected HTTP status codes documented.
- Frontend API error shape proposed.
- Retry guidance for 429, 5xx and POST operations.
- Suggested TypeScript API client outline.
- Root README links added.

## Reasons and goals

The Gateway public contract and forwarding tests are now in place. This PR defines how frontend applications should consume the Gateway without calling backend services directly.

## Architecture and behavior impact

- Documentation only.
- No runtime behavior change.
- Prepares future frontend module implementation.
