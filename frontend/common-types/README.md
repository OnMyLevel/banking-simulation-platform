# Common Types

Framework-neutral frontend types and helpers.

## Scope

```text
- header names
- API error type
- API metadata type
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
src/status.ts
src/index.ts
```

## Usage direction

React, Vue and Angular apps can later import these neutral contracts to keep Gateway error and metadata handling aligned.
