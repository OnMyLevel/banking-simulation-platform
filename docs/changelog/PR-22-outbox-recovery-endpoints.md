# PR #22 - feat outbox recovery endpoints

## Files changed

- `backend/core-banking-api/src/main/java/com/banking/core/domain/model/OutboxEvent.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/repository/OutboxEventRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/domain/exception/OutboxEventNotFoundException.java`
- `backend/core-banking-api/src/main/java/com/banking/core/application/facade/OutboxOpsFacade.java`
- `backend/core-banking-api/src/main/java/com/banking/core/api/controller/OutboxInternalController.java`
- `backend/core-banking-api/src/main/java/com/banking/core/api/response/OutboxEventResponse.java`
- `backend/core-banking-api/src/main/java/com/banking/core/api/mapper/OutboxEventApiMapper.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventJpaRepository.java`
- `backend/core-banking-api/src/main/java/com/banking/core/infrastructure/outbox/OutboxEventJpaAdapter.java`
- `backend/core-banking-api/src/test/java/com/banking/core/application/facade/OutboxOpsFacadeTest.java`
- `backend/core-banking-api/README.md`

## Concepts and features

- Internal outbox operation endpoint.
- Listing outbox events by status.
- Manual retry of a blocked event.
- Reset of `next_retry_at` to now.
- Response DTO exposing retry metadata.
- Not found error mapping for missing outbox event.

## Reasons and goals

When an event reaches the retry limit, operators need a controlled recovery path without direct database updates. These endpoints prepare the future admin or ops workflow.

## Architecture and behavior impact

- Adds internal endpoints under `/internal/outbox-events`.
- Keeps the public banking API unchanged.
- Prepares future security enforcement for admin or ops users.
