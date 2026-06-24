# PR #31 - docs core monitoring rules

## Files changed

- `docs/observability/core-signals.md`
- `backend/core-banking-api/README.md`

## Concepts and features

- Operational thresholds for Core Banking outbox metrics.
- Runbook for event sending issues.
- SQL queries for investigating outbox status and backlog.
- README link to the detailed monitoring guide.

## Metrics covered

```text
banking.outbox.delivery.success
banking.outbox.delivery.failure
banking.outbox.events{status="PENDING"}
banking.outbox.events{status="FAILED"}
banking.outbox.events{status="SENT"}
```

## Reasons and goals

PR #30 added the metrics. This PR explains how to use them in operations: what to watch, when to react, what SQL checks to run and how to recover after destination instability.

## Architecture and behavior impact

- Documentation only.
- No runtime code change.
- Prepares future integration with dashboard and monitoring tooling.
