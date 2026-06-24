# API Gateway signed access tests

## Purpose

The Gateway has a `jwt` profile for identity-based route decisions. These tests verify the profile with locally signed JWT values instead of mock authentication objects.

## Covered behavior

```text
- account routes reject requests without bearer access
- account routes accept signed USER access
- operation routes accept signed ADVISOR access
- operation routes reject signed OPS access
- role claims are mapped to Spring Security ROLE_* authorities
```

## Test design

The test suite creates an RSA key pair in memory.

```text
1. Test builds a signed JWT value with a roles claim.
2. Test configures the Gateway decoder with the matching public key.
3. Request is sent with Authorization: Bearer <value>.
4. Gateway decodes the value and applies route rules.
```

Backend service URLs point to unavailable local ports. This is intentional:

```text
- allowed requests reach routing and fail with 5xx because the backend is unavailable
- rejected requests fail at Gateway with 401 or 403 before routing
```

## Expected role behavior

| Role | Account routes | Operation routes |
| --- | --- | --- |
| `USER` | allowed | allowed |
| `ADVISOR` | allowed | allowed |
| `ADMIN` | allowed | allowed |
| `OPS` | not allowed for business routes | not allowed for business routes |

## Claim mapping

The Gateway reads this claim:

```json
{
  "roles": ["USER", "ADVISOR"]
}
```

The converter maps it to:

```text
ROLE_USER
ROLE_ADVISOR
```

Scope values are also preserved as `SCOPE_*` authorities for future use.
