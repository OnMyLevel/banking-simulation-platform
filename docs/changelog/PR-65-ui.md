# PR #65 - UI updates

## Files changed

- `frontend/angular-app/src/app/gateway-error.mapper.ts`
- `frontend/angular-app/src/app/gateway-error.mapper.spec.ts`
- `frontend/angular-app/src/app/advisor-dashboard.component.ts`
- `frontend/angular-app/src/app/advisor-dashboard.component.spec.ts`
- `frontend/angular-app/README.md`

## Changes

- Adds an error mapping helper.
- Reuses common frontend API error type.
- Reuses common status helpers.
- Keeps the dashboard visual behavior in place.
- Allows dashboard error state to receive mapped errors.
- Adds tests for error mapping.
- Updates dashboard tests for mapped error state.
- Updates the README.

## Behavior

```text
- error mapping is ready for future API service integration
- correlation id can still be shown as a support reference
- retry delay can be displayed when available
```

## Impact

- No backend runtime behavior change.
- No visual UI behavior change expected.
- Prepares future API service integration.
