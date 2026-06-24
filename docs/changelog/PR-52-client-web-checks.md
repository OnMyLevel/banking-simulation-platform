# PR #52 - Client web checks

## Files changed

- `.github/workflows/frontend-client-web.yml`
- `frontend/client-web-react/src/vite-env.d.ts`
- `docs/frontend/ui-pipeline-roadmap.md`

## Features

- Adds GitHub Actions workflow for `client-web-react`.
- Runs dependency install.
- Runs lint command.
- Runs unit checks.
- Runs build command.
- Adds Vite client type declarations for CSS imports.
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

## Fix applied after CI feedback

The build failed because TypeScript could not resolve the side-effect CSS import in `src/main.tsx`.

A Vite environment declaration file was added so TypeScript can resolve CSS and Vite client types during `tsc -b`.

## Architecture and behavior impact

- No backend runtime behavior change.
- Adds automated checks for the React UI.
- Prepares equivalent workflows for Vue and Angular after their setup PRs.
