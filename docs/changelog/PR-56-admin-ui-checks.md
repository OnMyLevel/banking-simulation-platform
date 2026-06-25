# PR #56 - Admin UI checks

## Files changed

- `.github/workflows/admin-ui.yml`

## Features

- Adds GitHub Actions workflow for `frontend/angular-app`.
- Runs dependency install.
- Runs lint command.
- Runs unit checks.
- Runs build command.

## Workflow commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

The admin UI setup is now merged. This PR applies the same automated quality gate approach already used for React and Vue UI apps.

## Architecture and behavior impact

- No backend runtime behavior change.
- Adds automated checks for the admin UI.
