# PR #49 - Client web project setup

## Files changed

- `frontend/client-web-react/package.json`
- `frontend/client-web-react/tsconfig.json`
- `frontend/client-web-react/tsconfig.node.json`
- `frontend/client-web-react/vite.config.ts`
- `frontend/client-web-react/vitest.config.ts`
- `frontend/client-web-react/eslint.config.js`
- `frontend/client-web-react/index.html`
- `frontend/client-web-react/src/main.tsx`
- `frontend/client-web-react/src/App.tsx`
- `frontend/client-web-react/src/styles.css`
- `frontend/client-web-react/README.md`

## Features

- React project setup.
- TypeScript configuration.
- Vite configuration.
- Vitest configuration.
- ESLint configuration.
- Basic application entrypoint.
- Placeholder client portal screen.
- Base CSS.
- README setup commands.

## Scripts

```text
npm run dev
npm run build
npm run test
npm run lint
```

## Reasons and goals

PR #48 added the client API layer. This PR makes the frontend project executable so future UI work can build on a working React setup.

## Architecture and behavior impact

- Adds frontend setup only.
- No backend runtime behavior change.
- Prepares account and operation screens.
