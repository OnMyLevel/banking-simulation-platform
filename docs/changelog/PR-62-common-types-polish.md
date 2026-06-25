# PR #62 - Common types polish

## Files changed

- `.github/workflows/common-types.yml`
- `frontend/common-types/package.json`
- `frontend/common-types/README.md`
- `frontend/common-types/src/index.ts`
- `frontend/common-types/src/retry.ts`
- `frontend/common-types/src/retry.test.ts`
- `frontend/common-types/src/status.ts`
- `frontend/common-types/src/status.test.ts`

## Fixes and improvements

- Adds package entrypoint metadata.
- Adds package exports metadata.
- Adds CI workflow for the common types package.
- Adds retry delay parsing helper.
- Adds tests for retry delay parsing.
- Adds success and not-found status helpers.
- Extends status helper tests.
- Updates README with the new exports and CI workflow.

## Commands covered by CI

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

The common frontend types package was introduced in PR #61. This PR hardens it before React, Vue and Angular start consuming it directly.

## Architecture and behavior impact

- No backend runtime behavior change.
- No UI runtime behavior change yet.
- Improves package readiness and automated validation.
