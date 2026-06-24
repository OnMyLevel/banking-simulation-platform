# PR #53 - Vue app setup

## Files changed

- `frontend/vue-app/package.json`
- `frontend/vue-app/tsconfig.json`
- `frontend/vue-app/vite.config.ts`
- `frontend/vue-app/vitest.config.ts`
- `frontend/vue-app/index.html`
- `frontend/vue-app/src/main.ts`
- `frontend/vue-app/src/Root.vue`
- `frontend/vue-app/src/Root.test.ts`
- `frontend/vue-app/src/vite-env.d.ts`
- `frontend/vue-app/README.md`
- `tests/scripts/run-local-tests.sh`

## Features

- Adds Vue application setup.
- Adds TypeScript setup.
- Adds Vite setup.
- Adds Vitest setup.
- Adds placeholder application shell.
- Adds first component test.
- Adds package scripts for local development and checks.
- Makes the local Docker suite wait longer for the Core Banking API.
- Adds service output on local suite startup failure.

## Commands

```text
npm install
npm run dev
npm run lint
npm run test
npm run build
```

## CI feedback fix

The local Docker suite timed out while waiting for the Core Banking API endpoint.

The startup wait is now configurable and longer by default:

```text
ACCOUNT_API_WAIT_SECONDS=240
CORE_API_WAIT_SECONDS=420
```

The script also prints Account Banking API and Core Banking API output before failing, which makes the next failure easier to diagnose.

## Reasons and goals

The project already has the React client setup and UI check plan. This PR adds the Vue application baseline before adding its dedicated workflow.

## Architecture and behavior impact

- Adds a frontend application skeleton.
- No backend runtime behavior change.
- Improves local suite stability in CI.
- Prepares a future Vue workflow PR.
