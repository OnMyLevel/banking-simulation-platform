# PR #58 - React account overview

## Files changed

- `frontend/client-web-react/src/App.tsx`
- `frontend/client-web-react/src/features/accounts/AccountOverview.tsx`
- `frontend/client-web-react/src/features/accounts/AccountOverview.test.tsx`
- `frontend/client-web-react/src/styles.css`
- `frontend/client-web-react/README.md`
- `frontend/angular-app/package.json`
- `frontend/angular-app/tsconfig.json`
- `.github/workflows/admin-ui.yml`

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
- Narrows the admin UI workflow trigger to Angular app changes.
- Pins Angular dependencies to a stable compatible range.
- Cleans deprecated TypeScript options in the Angular app.
- Adds the missing Angular Karma Jasmine HTML reporter dependency.
- Adds the missing Angular platform browser dynamic dependency required by Angular tests.

## Gateway behavior

```text
- Account data is loaded through accountsApi.
- Gateway errors are displayed through the normalized error model.
- X-Correlation-Id is shown as a support reference when available.
```

## CI feedback fix

The admin UI workflow was running on every frontend change and Angular dependencies were using `latest`, which pulled an incompatible Angular/TypeScript combination.

The fix now:

```text
- runs admin UI checks only for frontend/angular-app and admin-ui workflow changes
- pins Angular packages to ~18.2.0
- pins TypeScript to ~5.5.4
- removes deprecated TypeScript options from the Angular tsconfig
- uses Node 20 for the pinned Angular baseline
- adds karma-jasmine-html-reporter for Angular unit tests
- adds @angular/platform-browser-dynamic for Angular unit tests
```

## Reasons and goals

React, Vue and Angular baselines are now in place with CI and shared Gateway alignment. The next step is to start real UI behavior in the React client app.

## Architecture and behavior impact

- Adds a first customer-facing React screen.
- No backend runtime behavior change.
- Keeps frontend integration through the API Gateway client.
- Stabilizes the Angular CI baseline.
- Reduces unrelated CI runs for React-only frontend changes.
