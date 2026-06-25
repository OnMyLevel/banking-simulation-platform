# PR #57 - UI Gateway alignment

## Files changed

- `docs/frontend/ui-gateway-alignment.md`
- `docs/frontend/gateway-client-outline.md`
- `docs/frontend/multi-app-architecture.md`
- `tests/docker/docker-compose.tests.yml`

## Features

- Documents aligned Gateway client behavior for React, Vue and Angular apps.
- Defines common frontend API error shape.
- Defines common response metadata shape.
- Documents header handling rules.
- Documents status handling rules.
- Documents framework-specific implementation strategy.
- Updates existing frontend architecture and Gateway client outline links.
- Isolates Maven cache volumes per local Docker service to avoid concurrent Maven image startup conflicts.

## Shared behavior

```text
- configurable Gateway base URL
- Authorization header when access value exists
- X-Correlation-Id request and response handling
- common API error shape
- Retry-After handling for 429 responses
- no automatic retry of POST commands without explicit idempotency
```

## CI feedback fix

The local Docker suite failed during Core Banking API startup with:

```text
cp: cannot create regular file '/root/.m2/./settings-docker.xml': File exists
```

The Docker test compose file now uses separate Maven cache volumes for account, core and observability services.

## Reasons and goals

React, Vue and Angular baselines are now in place with CI workflows. Before adding real screens, all UI apps need a shared Gateway integration behavior.

## Architecture and behavior impact

- Documentation updates for UI Gateway alignment.
- No backend runtime behavior change.
- Improves local Docker test stability.
- Prepares future Vue and Angular Gateway client implementations.
