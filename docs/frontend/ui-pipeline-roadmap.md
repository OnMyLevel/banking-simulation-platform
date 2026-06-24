# UI pipeline roadmap

## Purpose

This roadmap defines how frontend checks should be added progressively for the three UI applications.

## Target applications

```text
client-web-react
business-web-vue
advisor-admin-angular
```

## Phase 1 - client web baseline

Target app:

```text
client-web-react
```

Checks:

```text
npm install
npm run lint
npm run test
npm run build
```

Goal:

```text
Prove that the React app and Gateway client layer can be installed, checked and built in GitHub Actions.
```

## Phase 2 - Vue and Angular baselines

After the Vue and Angular setup PRs, add the same baseline checks:

```text
business-web-vue
advisor-admin-angular
```

Each app must provide equivalent commands:

```text
npm run lint
npm run test
npm run build
```

## Phase 3 - browser journeys

Add Playwright once the first real screens exist.

Initial journeys:

```text
- application shell loads
- account page loads mocked data
- operation form validates user input
- access denied screen appears on 403
- throttling message appears on 429
```

## Phase 4 - API load checks

Add k6 or equivalent against the API Gateway public routes.

The main target is the API Gateway, not static frontend assets.

## Phase 5 - dependency and safety checks

Add scheduled and pull-request checks for:

```text
- dependency issues
- secrets in repository content
- unsafe client logging patterns
- missing correlation id handling in error flows
```

## Recommended first workflow

```text
.github/workflows/frontend-client-web.yml
```

Jobs:

```text
client-web-react-checks
```

Steps:

```text
checkout
setup node
npm install
npm run lint
npm run test
npm run build
```

## Merge gates

A UI pull request should not merge when:

```text
- install fails
- lint fails
- unit checks fail
- build fails
- the Gateway contract is broken
```
