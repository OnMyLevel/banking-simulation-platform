# Common Types

Framework-neutral frontend types and helpers.

## Scope

```text
- client field names
- API error type
- API metadata type
- API result type
- retry delay parser
- HTTP status helpers
```

This folder must not contain React, Vue or Angular components.

## Commands

```text
npm install
npm run lint
npm run test
npm run build
```

## Exports

```text
src/api-error.ts
src/names.ts
src/retry.ts
src/status.ts
src/index.ts
```

## CI

```text
.github/workflows/common-types.yml
```

## Usage direction

React, Vue and Angular apps can later import these neutral contracts to keep Gateway error, metadata and status handling aligned.
