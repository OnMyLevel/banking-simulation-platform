# PR #45 - Gateway public route contract

## Files changed

- `docs/architecture/api-gateway-public-contract.md`
- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewayRouteBehaviorTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewayJwtRouteBehaviorTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/route/GatewaySignedAccessRouteBehaviorTest.java`
- `backend/api-gateway/README.md`

## Features

- Clean public route contract documented.
- Duplicated public examples replaced in README.
- Route behavior tests updated to use clean account and operation paths.
- JWT profile route tests updated to use clean paths.
- Signed access route tests updated to use clean paths.

## Target public routes

```text
GET /api/accounts/{accountId}
POST /api/operations/credits
POST /api/operations/debits
POST /api/operations/transfers
```

## Reasons and goals

The Gateway should expose a frontend-friendly API contract. Public URLs should not repeat backend controller nouns or leak internal implementation details.

## Architecture and behavior impact

- Documentation and tests now promote clean public route names.
- Gateway route families remain unchanged.
- Backend services still own their business routes and business rules.
