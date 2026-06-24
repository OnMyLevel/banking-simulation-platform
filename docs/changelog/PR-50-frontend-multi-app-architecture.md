# PR #50 - Frontend multi-app architecture

## Files changed

- `docs/frontend/multi-app-architecture.md`
- `docs/frontend/shared-frontend-rules.md`
- `README.md`

## Features

- Multi-frontend architecture documented.
- Roles of React, Vue and Angular applications clarified.
- Shared Gateway contract rules documented.
- Shared frontend error handling rules documented.
- Shared header and retry rules documented.
- Root README links updated.

## Frontend applications

```text
client-web-react       -> individual customer portal
business-web-vue       -> business banking portal
advisor-admin-angular  -> advisor and admin portal
```

## Reasons and goals

The React client setup is now in place. Before adding Vue and Angular applications, the project needs a clear multi-app architecture to keep frontend integrations consistent.

## Architecture and behavior impact

- Documentation only.
- No runtime behavior change.
- Prepares Vue and Angular setup PRs.
