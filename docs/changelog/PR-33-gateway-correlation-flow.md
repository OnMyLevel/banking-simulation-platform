# PR #33 - feat Gateway correlation flow

## Files changed

- `backend/api-gateway/src/main/java/com/banking/gateway/filter/CorrelationIdFilter.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/filter/CorrelationIdFilterTest.java`
- `docs/architecture/api-gateway-flow.md`
- `backend/api-gateway/README.md`

## Concepts and features

- Gateway request trace header propagation.
- `X-Correlation-Id` generation when missing.
- Existing `X-Correlation-Id` preservation when provided by the client.
- Response header enrichment with the same value.
- API Gateway request algorithm documentation.

## Algorithm summary

```text
1. Receive client request.
2. Read X-Correlation-Id.
3. Generate UUID if missing or blank.
4. Add the header to the downstream request.
5. Route the request to the matching backend service.
6. Add the same header to the response.
7. Return the backend response to the client.
```

## Reasons and goals

The Gateway is now the platform entry point. This PR adds the first cross-cutting behavior needed for distributed troubleshooting before adding security, request logging and rate limiting.

## Architecture and behavior impact

- No business behavior change.
- Adds traceability for Gateway to backend requests.
- Prepares future log correlation across services.
