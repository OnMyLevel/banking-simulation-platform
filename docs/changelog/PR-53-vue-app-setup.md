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

## Features

- Adds Vue application setup.
- Adds TypeScript setup.
- Adds Vite setup.
- Adds Vitest setup.
- Adds placeholder application shell.
- Adds first component test.
- Adds package scripts for local development and checks.

## Commands

```text
npm install
npm run dev
npm run lint
npm run test
npm run build
```

## Reasons and goals

The project already has the React client setup and UI check plan. This PR adds the Vue application baseline before adding its dedicated workflow.

## Architecture and behavior impact

- Adds a frontend application skeleton.
- No backend runtime behavior change.
- Prepares a future Vue workflow PR.
