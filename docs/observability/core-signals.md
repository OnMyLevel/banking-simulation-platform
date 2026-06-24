# Core signals guide

## Purpose

Core Banking API exposes outbox metrics through Spring Boot Actuator. These signals help operators detect when event sending is slow, blocked or unstable.

## Metrics

```text
banking.outbox.delivery.success
banking.outbox.delivery.failure
banking.outbox.events{status="PENDING"}
banking.outbox.events{status="FAILED"}
banking.outbox.events{status="SENT"}
```

## Suggested thresholds

### Rows with FAILED status

Metric:

```text
banking.outbox.events{status="FAILED"}
```

Suggested threshold:

```text
value > 0 for 10 minutes
```

Operational meaning:

```text
At least one event could not be sent after retry handling.
```

Action:

```text
Check the selected destination, inspect the last_error column in core_schema.outbox_events, then retry the row when the destination is healthy again.
```

### Pending backlog grows

Metric:

```text
banking.outbox.events{status="PENDING"}
```

Suggested threshold:

```text
value increases for 15 minutes
```

Operational meaning:

```text
The relay is not draining events fast enough or the scheduled job is blocked.
```

Action:

```text
Check Core Banking API logs, verify the relay job is running, check database performance and confirm the selected destination is reachable.
```

### Send error counter increases quickly

Metric:

```text
banking.outbox.delivery.failure
```

Suggested threshold:

```text
increase over 5 minutes > 5
```

Operational meaning:

```text
The selected delivery target is unstable or unavailable.
```

Action:

```text
Check Observability API, Fluent Bit or Kafka according to banking.outbox.destination-type. Confirm network connectivity and timeout configuration.
```

### No send success while pending rows exist

Metrics:

```text
banking.outbox.delivery.success
banking.outbox.events{status="PENDING"}
```

Suggested threshold:

```text
success increase over 30 minutes = 0 and PENDING > 0
```

Operational meaning:

```text
Events exist but none are being sent successfully.
```

Action:

```text
Check relay scheduling, destination routing, sender configuration and recent application errors.
```

## SQL checks

Status summary:

```sql
SELECT destination_type, status, COUNT(*)
FROM core_schema.outbox_events
GROUP BY destination_type, status
ORDER BY destination_type, status;
```

Rows needing attention:

```sql
SELECT id, destination_type, retry_count, last_error, next_retry_at, created_at
FROM core_schema.outbox_events
WHERE status = 'FAILED'
ORDER BY created_at DESC;
```

Oldest pending rows:

```sql
SELECT id, destination_type, retry_count, next_retry_at, created_at
FROM core_schema.outbox_events
WHERE status = 'PENDING'
ORDER BY created_at ASC
LIMIT 20;
```

## Runbook

1. Identify whether the issue is backlog growth, send errors or rows in `FAILED`.
2. Check `banking.outbox.destination-type`.
3. Verify the target service or broker is reachable.
4. Inspect `last_error` for rows in `FAILED`.
5. Fix the destination or configuration issue.
6. Retry the affected rows through the internal operation endpoint.
7. Confirm that pending and failed counts go down.

## Internal operation endpoint

```http
POST /internal/outbox-events/{eventId}/retry
```

Access rule:

```text
OPS or ADMIN role required
```

## Notes

These thresholds are starting values for local and MVP environments. They should be adjusted after observing real traffic volume, relay frequency and destination stability.
