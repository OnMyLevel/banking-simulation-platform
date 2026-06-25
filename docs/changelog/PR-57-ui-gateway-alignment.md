# PR #57 - UI Gateway alignment

## Files changed

- `docs/frontend/ui-gateway-alignment.md`
- `docs/frontend/gateway-client-outline.md`
- `docs/frontend/multi-app-architecture.md`

## Features

- Documents aligned Gateway client behavior for React, Vue and Angular apps.
- Defines common frontend API error shape.
- Defines common response metadata shape.
- Documents header handling rules.
- Documents status handling rules.
- Documents framework-specific implementation strategy.
- Updates existing frontend architecture and Gateway client outline links.

## Shared behavior

```text
- configurable Gateway base URL
- Authorization header when access value exists
- X-Correlation-Id request and response handling
- common API error shape
- Retry-After handling for 429 responses
- no automatic retry of POST commands without explicit idempotency
```

## Reasons and goals

React, Vue and Angular baselines are now in place with CI workflows. Before adding real screens, all UI apps need a shared Gateway integration behavior.

## Architecture and behavior impact

- Documentation only.
- No runtime behavior change.
- Prepares future Vue and Angular Gateway client implementations.
