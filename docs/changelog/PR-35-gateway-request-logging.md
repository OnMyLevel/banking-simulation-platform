# PR #35 - feat Gateway request logging

## Files changed

- `backend/api-gateway/src/main/java/com/banking/gateway/logging/GatewayRequestLoggingFilter.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/logging/GatewayRequestLoggingFilterTest.java`
- `docs/architecture/api-gateway-logs.md`
- `backend/api-gateway/README.md`

## Concepts and features

- Gateway technical request logging.
- Correlation id included in Gateway logs.
- HTTP method, request path, status and duration logged.
- Error exchange logging with exception class.
- Sensitive data exclusion rule.
- Documentation of Gateway log format and usage.

## Log format

```text
gateway_request method=<HTTP_METHOD> path=<REQUEST_PATH> status=<HTTP_STATUS> durationMs=<DURATION_MS> correlationId=<X_CORRELATION_ID>
```

Error format:

```text
gateway_request_error method=<HTTP_METHOD> path=<REQUEST_PATH> durationMs=<DURATION_MS> correlationId=<X_CORRELATION_ID> error=<ERROR_CLASS>
```

## Reasons and goals

The Gateway now has routing, correlation id propagation, route rules and traffic budgets. This PR adds visibility into Gateway decisions without logging sensitive business data.

## Architecture and behavior impact

- Adds technical logs around Gateway request processing.
- Does not log request bodies, response bodies, credentials, cookies or business data.
- Prepares observability dashboards around status, latency and correlation id troubleshooting.
