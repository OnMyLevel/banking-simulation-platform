# PR #51 - UI check plan

## Files changed

- `docs/frontend/ui-check-plan.md`
- `docs/frontend/ui-pipeline-roadmap.md`
- `docs/frontend/multi-app-architecture.md`

## Features

- UI check plan for React, Vue and Angular applications.
- Required commands per UI documented.
- Static checks, unit checks and integration checks documented.
- Browser journey check strategy documented.
- Load-oriented check strategy documented.
- Frontend safety check strategy documented.
- GitHub Actions roadmap documented.
- Multi-app architecture updated with links to the UI check plan.

## Required baseline commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

The project now has a React setup and a multi-app frontend architecture. Before adding Vue and Angular setups, the UI quality strategy must be clear and consistent.

## Architecture and behavior impact

- Documentation only.
- No runtime behavior change.
- Prepares frontend workflows and future UI application setup PRs.
