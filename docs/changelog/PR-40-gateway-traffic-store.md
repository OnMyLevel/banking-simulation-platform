# PR #40 - feat Gateway traffic store

## Files changed

- `backend/api-gateway/pom.xml`
- `backend/api-gateway/src/main/resources/application.yml`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/GatewayTrafficProperties.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficStoreMode.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficBudgetDecision.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficBudgetStore.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/LocalTrafficBudgetStore.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/RedisTrafficBudgetStore.java`
- `backend/api-gateway/src/main/java/com/banking/gateway/traffic/TrafficBudgetFilter.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/traffic/TrafficBudgetFilterTest.java`
- `backend/api-gateway/src/test/java/com/banking/gateway/traffic/GatewayTrafficPropertiesTest.java`
- `docs/architecture/api-gateway-traffic-store.md`

## Features

- Traffic budget store abstraction.
- Local store kept as default.
- Redis store added as opt-in mode.
- Store mode configured with `BANKING_GATEWAY_TRAFFIC_STORE_MODE`.
- Traffic filter delegates counter management to a store.
- Tests adapted to the store abstraction.

## Store modes

```text
IN_MEMORY
REDIS
```

## Reasons and goals

The previous traffic budget used local memory only. That is fine for one Gateway instance, but it does not share counters across multiple Gateway instances. This PR prepares a shared mode while keeping local startup simple.

## Architecture and behavior impact

- Default local behavior remains `IN_MEMORY`.
- Redis mode is opt-in.
- Integration tests for Redis are left for the next PR.
