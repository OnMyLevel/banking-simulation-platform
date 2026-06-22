# Changelog

This file tracks project changes by pull request.

For each PR, document:

- files changed;
- concepts or features introduced;
- reasons and goals of the change;
- architecture or behavior impact.

## PR #13 - docs test evolution strategy

### Files changed

- `tests/README.md`
- `CHANGELOG.md`

### Concepts and features

- Continuous evolution strategy for automated tests.
- Required test update rules for future backend PRs.
- Naming convention for functional scenarios.
- Test ownership guidance.

### Reasons and goals

The local test suite introduced in PR #12 must evolve with the product. This PR documents how each future API, business rule, error code, security rule, or service dependency change should update automated tests.

### Architecture and behavior impact

- Establishes `tests/` as the executable regression suite for the platform.
- Makes test maintenance part of the regular PR workflow.
- Does not change runtime behavior.

## PR #12 - test local automation suite

### Files changed

- `tests/docker/docker-compose.tests.yml`
- `tests/scripts/run-local-tests.sh`
- `tests/scripts/wait-for-http.sh`
- `tests/postman/Banking-Simulation.postman_collection.json`
- `tests/postman/local-docker.postman_environment.json`
- `tests/load/core-banking-smoke-load.js`
- `tests/security/.gitkeep`
- `tests/README.md`

### Concepts and features

- Local Docker test environment.
- Functional API tests with Newman and Postman collection.
- Smoke load test with k6.
- Baseline security scan with OWASP ZAP.
- One command script to start services and run tests.

### Reasons and goals

Manual startup and Postman execution are too slow and error-prone for daily validation. This PR creates an automated local test suite that can be run with one command.

### Architecture and behavior impact

- Adds a repeatable local quality gate.
- Keeps test assets outside production services.
- Makes functional, load and security checks executable in Docker.

## PR #11 - feat core history pagination

### Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/api/controller/CoreBankingController.java`
- `backend/core-banking-api/src/main/java/com/banking/core/api/response/OperationHistoryResponse.java`
- `backend/core-banking-api/src/test/java/com/banking/core/api/controller/CoreBankingControllerTest.java`
- `backend/core-banking-api/src/main/resources/openapi/core-banking-api.yaml`
- `backend/core-banking-api/README.md`

### Concepts and features

- Paginated operation history endpoint.
- Operation history response wrapper.
- Pagination guard for limit and offset.
- Controller test for operation history.
- OpenAPI response schema for history.

### Reasons and goals

Operation history should not return an unbounded list. This PR exposes limit and offset parameters and returns pagination metadata to make clients safer and easier to implement.

### Architecture and behavior impact

- Keeps pagination logic at the API boundary.
- Preserves the facade and domain service contract.
- Standardizes history responses with items, limit, offset and nextOffset.

## PR #10 - feat core resilience

### Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/account/AccountClientProperties.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/account/AccountClientConfiguration.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/account/HttpAccountClient.java`
- `backend/core-banking-api/src/main/resources/application.yml`
- `backend/core-banking-api/src/main/resources/application-docker.yml`
- `backend/core-banking-api/src/test/java/com/banking/core/infrastructure/account/HttpAccountClientTest.java`
- `backend/core-banking-api/README.md`

### Concepts and features

- HTTP client connect timeout.
- HTTP client read timeout.
- More explicit remote account error handling.
- Unit tests for the account HTTP adapter.

### Reasons and goals

Core Banking API depends on Account Banking API before executing sensitive operations. This PR avoids unbounded waiting and makes remote dependency failures explicit.

### Architecture and behavior impact

- Keeps account dependency handling inside the infrastructure adapter.
- Adds configurable timeout values for local and Docker profiles.
- Maps missing accounts and remote service errors consistently.

## PR #9 - feat core account status checks

### Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/domain/service/CoreBankingService.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/port/AccountClient.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/model/AccountSnapshot.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/account/**`
- `backend/core-banking-api/src/main/java/com/banking/core/api/error/CoreExceptionHandler.java`
- `backend/core-banking-api/src/test/java/com/banking/core/domain/service/CoreBankingServiceTest.java`
- `backend/core-banking-api/src/main/resources/application.yml`
- `backend/core-banking-api/src/main/resources/application-docker.yml`
- `backend/core-banking-api/README.md`

### Concepts and features

- Account client port in the core domain.
- HTTP adapter to call Account Banking API.
- Account status validation before credit, debit and transfer operations.
- Error mapping for missing, inactive, and unavailable account dependencies.

### Reasons and goals

Core banking operations must not run against closed or blocked accounts. This PR connects core banking to the account context so account status is verified before money movement is recorded.

### Architecture and behavior impact

- Introduces a cross-service dependency from Core Banking API to Account Banking API.
- Keeps the dependency behind a domain port.
- Preserves the domain service as the place where banking rules are enforced.
- Adds configuration for local and Docker account API URLs.

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
