# PR #55 - Angular app setup

## Files changed

- `frontend/angular-app/package.json`
- `frontend/angular-app/angular.json`
- `frontend/angular-app/tsconfig.json`
- `frontend/angular-app/tsconfig.app.json`
- `frontend/angular-app/tsconfig.spec.json`
- `frontend/angular-app/src/index.html`
- `frontend/angular-app/src/main.ts`
- `frontend/angular-app/src/styles.css`
- `frontend/angular-app/src/app/app.component.ts`
- `frontend/angular-app/src/app/app.component.spec.ts`
- `frontend/angular-app/README.md`

## Features

- Adds Angular application setup.
- Adds TypeScript setup.
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

The project already has React and Vue application baselines with dedicated workflows. This PR adds the Angular application baseline before adding its workflow.

## Architecture and behavior impact

- Adds a frontend application skeleton.
- No backend runtime behavior change.
- Prepares a future Angular workflow PR.
