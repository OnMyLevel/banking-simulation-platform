# Gateway identity model

## Purpose

The API Gateway is the platform entry point. It is the right place to validate caller identity before forwarding traffic to backend APIs.

## Current MVP mode

The default local profile keeps the current MVP behavior:

```text
- health and info endpoints are public
- user routes stay public for MVP onboarding
- account and operation routes require an authenticated caller
- internal routes are refused at the Gateway boundary
```

This mode is kept outside the `jwt` profile so local Docker and local tests stay simple.

## Target identity mode

The target mode uses the `jwt` Spring profile.

Expected behavior:

```text
- validate signed access tokens at Gateway level
- keep health and info public
- keep internal routes refused from the public Gateway
- allow account and operation routes only for business roles
- forward accepted requests to backend APIs
```

## Target roles

```text
USER
ADVISOR
ADMIN
OPS
```

Role intent:

| Role | Purpose |
| --- | --- |
| `USER` | End customer access. |
| `ADVISOR` | Advisor access to customer and account views. |
| `ADMIN` | Administrative access. |
| `OPS` | Operations and platform support access. |

## Target route matrix

| Route | Target rule |
| --- | --- |
| `/actuator/health` | Public |
| `/actuator/info` | Public |
| `/api/users/**` | Public for MVP, later split by endpoint |
| `/api/accounts/**` | `USER`, `ADVISOR`, or `ADMIN` |
| `/api/operations/**` | `USER`, `ADVISOR`, or `ADMIN` |
| `/internal/**` | Refused by public Gateway |

## Migration steps

```text
1. Keep the local profile as the MVP fallback.
2. Add the resource server dependency to the Gateway.
3. Add role constants used by Gateway access rules.
4. Add a dedicated jwt profile for token-based access rules.
5. Later, configure issuer or JWK location from the deployment environment.
6. Later, add integration tests with signed tokens.
7. Later, forward trusted identity claims to backend APIs only if required.
```

## Boundaries

The Gateway should validate entry-point identity. Backend services must still own their business rules.

The Gateway should not decide whether a transfer is allowed, whether an account can be debited, or whether a business action is valid. Those decisions stay in the owning service.
