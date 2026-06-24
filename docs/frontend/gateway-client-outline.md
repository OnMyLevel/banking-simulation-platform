# Gateway client outline

## Purpose

This document proposes a first frontend API client structure for calling the API Gateway.

It is not an implementation file. It is a contract outline for the future frontend module.

## Responsibilities

The Gateway client should centralize cross-cutting HTTP behavior:

```text
- base URL configuration
- bearer access value injection
- correlation id propagation
- JSON parsing
- API error normalization
- Retry-After handling
```

## Proposed TypeScript shape

```ts
export type GatewayClientOptions = {
  baseUrl: string;
  getAccessValue?: () => string | undefined;
  createCorrelationId?: () => string;
};

export type GatewayRequestOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  path: string;
  body?: unknown;
  correlationId?: string;
};

export type GatewayResponse<T> = {
  data: T;
  correlationId?: string;
};

export type GatewayApiError = {
  status: number;
  message: string;
  code?: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};
```

## Request algorithm

```text
1. Build the full URL from baseUrl and path.
2. Add Content-Type when body is present.
3. Add Authorization when an access value exists.
4. Add X-Correlation-Id when provided or generated.
5. Send request.
6. Read X-Correlation-Id from response.
7. If response is ok, parse and return data.
8. If response is not ok, normalize error and throw it.
```

## Suggested domain clients

### Accounts API

```ts
export function getAccount(accountId: string): Promise<GatewayResponse<AccountDto>>;
```

### Operations API

```ts
export function createCredit(command: CreateCreditCommand): Promise<GatewayResponse<OperationDto>>;
export function createDebit(command: CreateDebitCommand): Promise<GatewayResponse<OperationDto>>;
export function createTransfer(command: CreateTransferCommand): Promise<GatewayResponse<OperationDto>>;
```

### Users API

```ts
export function getCurrentUser(): Promise<GatewayResponse<UserDto>>;
```

## Error display guidance

```text
401 -> ask user to sign in again
403 -> show access denied
429 -> show temporary throttling message and respect Retry-After
5xx -> show technical error with correlation id
```

## Notes

POST operation calls should not be retried automatically until an idempotency mechanism is defined.
