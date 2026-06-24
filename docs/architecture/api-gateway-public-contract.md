# API Gateway public contract

## Purpose

The API Gateway is the public API entry point for frontend clients. Public routes should be stable, readable and not leak internal service naming details.

This document defines the target public route contract.

## Route principles

```text
- keep public URLs short and readable
- avoid repeated service nouns
- keep backend paths hidden behind the Gateway
- keep route rules and telemetry grouped by route family
- keep backend services responsible for business rules
```

## Public route families

| Public route family | Target backend service |
| --- | --- |
| `/api/users/**` | User Management API |
| `/api/accounts/**` | Account Banking API |
| `/api/operations/**` | Core Banking API |

## Target public routes

### Accounts

| Public route | Purpose |
| --- | --- |
| `GET /api/accounts/{accountId}` | Read one account. |
| `GET /api/accounts` | List accounts when supported by backend. |

### Operations

| Public route | Purpose |
| --- | --- |
| `POST /api/operations/credits` | Create a credit operation. |
| `POST /api/operations/debits` | Create a debit operation. |
| `POST /api/operations/transfers` | Create a transfer operation. |
| `GET /api/operations/history` | Read operation history when supported by backend. |

## Avoided public routes

These routes are considered implementation-shaped and should not be promoted to the frontend contract:

```text
/api/accounts/accounts/{accountId}
/api/operations/operations/credits
/api/operations/operations/debits
/api/operations/operations/transfers
```

## Compatibility rule

The Gateway can still forward requests to backend paths with `StripPrefix` or route filters. The public API contract should stay clean even when backend controllers keep their own internal base paths.

## Test expectations

Public route tests must verify:

```text
- clean account routes are protected by Gateway access rules
- clean operation routes are protected by Gateway access rules
- clean operation routes are accepted for allowed jwt roles
- old duplicated examples are removed from documentation
```

## Future work

```text
- add backend stubs to verify exact path forwarding
- define frontend API client methods from this contract
- version the public contract if breaking changes are needed
```
