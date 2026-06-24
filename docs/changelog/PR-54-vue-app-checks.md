# PR #54 - Vue app checks

## Files changed

- `.github/workflows/frontend-vue-app.yml`
- `docs/frontend/ui-pipeline-roadmap.md`

## Features

- Adds GitHub Actions workflow for `frontend/vue-app`.
- Runs dependency install.
- Runs lint command.
- Runs unit checks.
- Runs build command.
- Limits workflow execution to Vue app changes.
- Updates UI pipeline roadmap.

## Workflow commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

The Vue app setup is now merged. This PR applies the same automated quality gate approach already used for the React client app.

## Architecture and behavior impact

- No backend runtime behavior change.
- Adds automated checks for the Vue UI.
- Prepares the later Angular setup and Angular workflow PRs.
