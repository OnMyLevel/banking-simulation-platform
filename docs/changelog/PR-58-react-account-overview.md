# PR #58 - React account overview

## Files changed

- `frontend/client-web-react/src/App.tsx`
- `frontend/client-web-react/src/features/accounts/AccountOverview.tsx`
- `frontend/client-web-react/src/features/accounts/AccountOverview.test.tsx`
- `frontend/client-web-react/src/styles.css`
- `frontend/client-web-react/README.md`

## Features

- Adds the first React account overview screen.
- Uses the existing Gateway client and accounts API layer.
- Handles loading state.
- Handles successful account data rendering.
- Handles normalized API errors.
- Displays correlation id when available.
- Displays Retry-After guidance for throttling errors.
- Adds component tests for success and error states.
- Updates the React app README.

## Gateway behavior

```text
- Account data is loaded through accountsApi.
- Gateway errors are displayed through the normalized error model.
- X-Correlation-Id is shown as a support reference when available.
```

## Reasons and goals

React, Vue and Angular baselines are now in place with CI and shared Gateway alignment. The next step is to start real UI behavior in the React client app.

## Architecture and behavior impact

- Adds a first customer-facing React screen.
- No backend runtime behavior change.
- Keeps frontend integration through the API Gateway client.
