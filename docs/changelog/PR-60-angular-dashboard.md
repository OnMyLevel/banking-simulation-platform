# PR #60 - Angular dashboard

## Files changed

- `frontend/angular-app/src/app/app.component.ts`
- `frontend/angular-app/src/app/app.component.spec.ts`
- `frontend/angular-app/src/app/advisor-dashboard.component.ts`
- `frontend/angular-app/src/app/advisor-dashboard.component.spec.ts`
- `frontend/angular-app/README.md`

## Features

- Adds the first Angular advisor dashboard screen.
- Adds ready, empty and error states.
- Displays a support reference in error state.
- Wires the dashboard into the Angular root component.
- Adds component tests for ready, empty and error states.
- Updates the Angular app README.

## Gateway preparation

```text
- The dashboard is input-driven for now.
- Future PRs can connect it to an Angular Gateway service or interceptor.
- Error state already supports a support reference value.
```

## Reasons and goals

React now has an account overview screen and Vue has a company dashboard. This PR gives the Angular app its first advisor-oriented dashboard shell before connecting real Gateway data.

## Architecture and behavior impact

- Adds first Angular advisor screen.
- No backend runtime behavior change.
- Prepares future Angular Gateway service integration.
