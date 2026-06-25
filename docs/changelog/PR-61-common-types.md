# PR #61 - Common frontend types

## Files changed

- `frontend/common-types/package.json`
- `frontend/common-types/tsconfig.json`
- `frontend/common-types/README.md`
- `frontend/common-types/src/api-error.ts`
- `frontend/common-types/src/names.ts`
- `frontend/common-types/src/status.ts`
- `frontend/common-types/src/status.test.ts`
- `frontend/common-types/src/index.ts`

## Features

- Adds a framework-neutral frontend package.
- Adds common API error type.
- Adds common API metadata type.
- Adds response wrapper type.
- Adds common client field names.
- Adds HTTP status helpers.
- Adds unit tests for status helpers.
- Adds README and package scripts.

## Commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Reasons and goals

React, Vue and Angular now each have their first screen. This PR adds a neutral package for contracts and helpers that can be reused before wiring Vue and Angular to real Gateway data.

## Architecture and behavior impact

- No backend runtime behavior change.
- No UI runtime behavior change yet.
- Prepares consistent frontend Gateway integration across React, Vue and Angular.
