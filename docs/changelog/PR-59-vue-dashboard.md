# PR #59 - Vue dashboard

## Files changed

- `frontend/vue-app/src/Root.vue`
- `frontend/vue-app/src/Root.test.ts`
- `frontend/vue-app/src/features/company/CompanyDashboard.vue`
- `frontend/vue-app/src/features/company/CompanyDashboard.test.ts`
- `frontend/vue-app/README.md`

## Features

- Adds the first Vue dashboard screen.
- Adds ready, empty and error states.
- Displays a support reference in error state.
- Wires the dashboard into the Vue root component.
- Adds component tests for ready, empty and error states.
- Updates the Vue app README.

## Gateway preparation

```text
- The dashboard is prop-driven for now.
- Future PRs can connect it to a Vue Gateway client or composable.
- Error state already supports a support reference value.
```

## Reasons and goals

React now has its first account overview screen. This PR gives the Vue app its first business-oriented dashboard shell before connecting real Gateway data.

## Architecture and behavior impact

- Adds first Vue screen.
- No backend runtime behavior change.
- Prepares future Vue Gateway client integration.
