# API Gateway request logs

## Purpose

The Gateway emits one structured log per request. The goal is to make entry-point behavior traceable before requests move to backend APIs.

## Log fields

Successful exchange:

```text
gateway_request method=<HTTP_METHOD> path=<REQUEST_PATH> status=<HTTP_STATUS> durationMs=<DURATION_MS> correlationId=<X_CORRELATION_ID>
```

Failed exchange:

```text
gateway_request_error method=<HTTP_METHOD> path=<REQUEST_PATH> durationMs=<DURATION_MS> correlationId=<X_CORRELATION_ID> error=<ERROR_CLASS>
```

## Fields explained

| Field | Meaning |
| --- | --- |
| `method` | HTTP method received by the Gateway. |
| `path` | Request path received by the Gateway. |
| `status` | Response status returned by the Gateway. |
| `durationMs` | Time spent processing the Gateway exchange. |
| `correlationId` | Value of `X-Correlation-Id`. |
| `error` | Exception class for failed exchanges. |

## Sensitive data rule

The Gateway request log must not print:

```text
- request body
- response body
- Authorization header
- cookies
- personal data
- bank account details
- operation amounts
```

The log only contains technical routing data.

## Operational usage

Use the correlation id to connect Gateway logs with backend service logs and observability events.

Common troubleshooting examples:

```text
- 401 or 403 from Gateway rules
- 429 from traffic budget filter
- 5xx returned by a backend API
- slow requests visible through durationMs
```

## Example

```text
gateway_request method=POST path=/api/operations/operations/credits status=200 durationMs=42 correlationId=7f4d1d5e-6a4c-4c44-aea5-1746d0d5979c
```

## Future improvements

```text
- export logs to the observability stack
- add route id to the log line
- add client family without logging personal data
- add dashboard panels for status and latency
```
