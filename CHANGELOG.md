# Changelog

This file tracks project changes by pull request.

For each PR, document:

- files changed;
- concepts or features introduced;
- reasons and goals of the change;
- architecture or behavior impact.

## PR #8 - feat core banking business rules

### Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/domain/service/CoreBankingService.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/exception/InsufficientFundsException.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/repository/OperationRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/persistence/**`
- `backend/core-banking-api/src/test/java/com/banking/core/**`
- `backend/core-banking-api/src/main/resources/openapi/core-banking-api.yaml`
- `backend/core-banking-api/README.md`

### Concepts and features

- Balance projection from existing operations.
- Currency-specific balance checks.
- Account-level transactional guard before debit and transfer balance validation.
- Insufficient funds rule for debit and transfer operations.
- Repository integration test with Testcontainers.
- OpenAPI contract for core banking operations.

### Reasons and goals

This PR moves core banking from a basic operation recording service to a first business-ready foundation. It prevents debit and transfer operations from creating negative balances, including under concurrent requests for the same account, and avoids mixing balances across currencies.

### Architecture and behavior impact

- Adds balance projection to the repository port.
- Keeps balance validation in the domain service.
- Adds a repository-level account guard for sensitive balance checks.
- Filters balance projections by currency.
- Adds API error mapping for insufficient funds.
- Documents the core banking contract.

## PR #7 - feat core banking API foundation

### Files changed

- `backend/core-banking-api/**`
- `.github/workflows/dev-to-main.yml`

### Concepts and features

- Spring Boot foundation for Core Banking API.
- Operation domain model.
- Credit, debit, transfer, and history endpoints.
- Idempotency-Key support for write operations.
- JPA adapter and Flyway migration for `core_schema.operations`.

### Reasons and goals

This PR starts the banking operation context. It provides the first technical foundation for recording core banking operations.

### Architecture and behavior impact

- Adds the third backend service.
- Introduces operation history persistence.
- Adds idempotency protection to avoid duplicate operation execution.
- Extends CI validation to include `core-banking-api`.

## PR #6 - docs add project changelog

### Files changed

- `CHANGELOG.md`

### Concepts and features

- Project changelog by pull request.
- Structured history for files, concepts, goals, and impacts.

### Reasons and goals

The project needs a clear, readable history of architecture and code decisions to support reviews and onboarding.

### Architecture and behavior impact

- Improves project documentation and traceability.
- Does not change runtime behavior.

## PR #5 - feat account banking list endpoint

### Files changed

- `backend/account-banking-api/src/main/java/com/banking/account/api/controller/AccountController.java`
- `backend/account-banking-api/src/main/java/com/banking/account/api/response/AccountsResponse.java`
- `backend/account-banking-api/src/test/java/com/banking/account/api/controller/AccountControllerTest.java`
- `backend/account-banking-api/src/main/resources/openapi/account-banking-api.yaml`
- `backend/account-banking-api/README.md`

### Concepts and features

- Account listing endpoint filtered by owner.
- Basic pagination with `limit`, `offset`, and `nextOffset`.
- Controller test coverage for account listing.
- OpenAPI response schema for account collections.

### Reasons and goals

The account service already had repository and domain support for owner-based account lookup. This PR exposes that capability through the REST API and documents the contract.

### Architecture and behavior impact

- Adds a read endpoint to the account API.
- Keeps business logic outside the controller.
- Preserves DTO-based API responses.
- Keeps pagination bounded at the API boundary.

## PR #4 - feat account banking business rules

### Files changed

- Account domain model and service classes.
- Account repository and JPA adapter.
- Account error handling classes.
- Account service and repository tests.
- Minimal OpenAPI contract.

### Concepts and features

- Active account business checks.
- Closed account business checks.
- Owner account lookup contract.
- Repository pagination support.
- Error mapping for account status rules.

### Reasons and goals

This PR strengthens the account domain before adding core banking operations. It makes account status rules explicit and reusable by future services such as core banking.

### Architecture and behavior impact

- Moves account status rules into the domain service.
- Adds a clear path for core banking to verify account status.
- Improves error consistency for blocked or closed accounts.

## PR #3 - feat account banking API foundation

### Files changed

- `backend/account-banking-api/**`
- `.github/workflows/dev-to-main.yml`

### Concepts and features

- Spring Boot foundation for Account Banking API.
- Account domain model.
- Account DTOs and REST controller.
- Facade, domain repository, JPA adapter, JPA repository, and mapper.
- Flyway schema migration for `account_schema.accounts`.
- Tests for domain, controller, and persistence.

### Reasons and goals

This PR starts the account context after stabilizing user management. The goal is to provide the account foundation needed by core banking operations.

### Architecture and behavior impact

- Adds a new microservice module.
- Uses the same layered architecture rules as `user-management-api`.
- Avoids `ddl-auto: create-drop` and uses Flyway for schema creation.
- Returns HTTP 404 for missing accounts.

## PR #2 - feat user management business rules

### Files changed

- `backend/user-management-api/src/main/java/com/banking/user/domain/service/UserService.java`
- `backend/user-management-api/src/main/java/com/banking/user/domain/exception/**`
- `backend/user-management-api/src/main/java/com/banking/user/api/error/**`
- User service and repository tests.

### Concepts and features

- User domain service.
- Business exceptions for user rules.
- `USER_NOT_FOUND` handling.
- `EMAIL_ALREADY_USED` handling.
- Global exception handler.
- Unit and integration tests.

### Reasons and goals

The initial user service skeleton needed real domain rules and stable error handling before other services depended on it.

### Architecture and behavior impact

- Moves user creation and lookup rules into the domain layer.
- Replaces generic exceptions with explicit business errors.
- Improves API error consistency.

## PR #1 - feat user management API skeleton

### Files changed

- `backend/user-management-api/**`
- `docs/coding-rules/**`
- `.github/workflows/dev-to-main.yml`
- `docker-compose.yml`

### Concepts and features

- Initial User Management API skeleton.
- Clean and hexagonal architecture conventions.
- User domain model and DTOs.
- Persistence adapter foundation.
- Docker Compose integration.
- CI workflow for backend services.

### Reasons and goals

This PR creates the first real backend service and establishes the coding structure to reuse across the platform.

### Architecture and behavior impact

- Establishes the reference backend layering pattern.
- Adds the first persistence-backed service.
- Starts enforcing backend validation through CI.

## Future entries template

```markdown
## PR #X - title

### Files changed

- `path/to/file`

### Concepts and features

- Feature or concept introduced.

### Reasons and goals

Explain why the change exists.

### Architecture and behavior impact

Explain what changes for the architecture, API, data model, or runtime behavior.
```
