# PR #63 - React common types usage

## Files changed

- `frontend/client-web-react/src/api/apiErrors.ts`
- `frontend/client-web-react/src/api/gatewayClient.ts`
- `frontend/client-web-react/README.md`

## Features

- Reuses the common frontend API error type in the React API layer.
- Reuses common status helpers for React API error messages.
- Reuses common field names for Authorization, correlation id and retry delay handling.
- Reuses common retry delay parsing.
- Keeps the existing React Gateway client behavior.
- Updates the React README.

## Gateway behavior

```text
- Authorization is still added when an access value exists.
- X-Correlation-Id is still sent and read from responses.
- Retry-After is still exposed as retryAfterSeconds.
- Existing normalized error behavior remains in place.
```

## Reasons and goals

The common frontend types package is now available and hardened. React has the most complete Gateway client, so it is the first UI app to consume the common helpers.

## Architecture and behavior impact

- No backend runtime behavior change.
- No UI behavior change expected.
- Reduces duplication in React Gateway handling.
- Prepares the same migration pattern for Vue and Angular.
