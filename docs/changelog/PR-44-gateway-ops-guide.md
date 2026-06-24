# PR #44 - docs Gateway operations guide

## Files changed

- `docs/observability/gateway-ops-guide.md`
- `backend/api-gateway/README.md`

## Features

- Gateway operations guide.
- Metrics to watch for the Gateway.
- Useful Actuator metric queries.
- Suggested dashboard panels.
- Route family reference.
- Starting watch thresholds for 401, 403, 429, 5xx and p95 duration.
- Investigation flow using `X-Correlation-Id`.
- Common cases for 401, 403, 429 and 5xx responses.

## Reasons and goals

PR #43 added Gateway telemetry. This PR explains how to use those signals during operational analysis.

## Architecture and behavior impact

- Documentation only.
- No runtime behavior change.
- Prepares future dashboard implementation.
