# PR #64 - Vue updates

## Files changed

- `frontend/vue-app/src/composables/useGatewayError.ts`
- `frontend/vue-app/src/composables/useGatewayError.test.ts`
- `frontend/vue-app/src/features/company/CompanyDashboard.vue`
- `frontend/vue-app/src/features/company/CompanyDashboard.test.ts`
- `frontend/vue-app/README.md`

## Changes

- Adds a Vue error mapping composable.
- Reuses common frontend API error type.
- Reuses common status helpers.
- Keeps the dashboard visual behavior in place.
- Allows dashboard error state to receive mapped errors.
- Adds tests for Vue error mapping.
- Updates dashboard tests for mapped error state.
- Updates the Vue README.

## Behavior

```text
- error mapping is ready for future API client integration
- correlation id can still be shown as a support reference
- retry delay can be displayed when available
```

## Impact

- No backend runtime behavior change.
- No visual UI behavior change expected.
- Prepares future Vue API client integration.
