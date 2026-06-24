# PR #52 - Client web checks

## Files changed

- `.github/workflows/frontend-client-web.yml`
- `docs/frontend/ui-pipeline-roadmap.md`

## Features

- Adds GitHub Actions workflow for `client-web-react`.
- Runs dependency install.
- Runs lint command.
- Runs unit checks.
- Runs build command.
- Limits workflow execution to client web changes.
- Updates UI pipeline roadmap.

## Workflow commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

The UI check plan is now documented. This PR applies the first automated quality gate to the React client app.

## Architecture and behavior impact

- No backend runtime behavior change.
- Adds automated checks for the React UI.
- Prepares equivalent workflows for Vue and Angular after their setup PRs.
