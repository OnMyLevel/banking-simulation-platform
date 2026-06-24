package com.banking.gateway.traffic;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisTrafficBudgetStoreTest {
    @Test
    void shouldAllowRequestsWithinBudget() {
        ReactiveStringRedisTemplate template = mock(ReactiveStringRedisTemplate.class);
        ReactiveValueOperations<String, String> valueOperations = mock(ReactiveValueOperations.class);
        when(template.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("gateway:traffic:client-1:operations")).thenReturn(Mono.just(1L));
        when(template.expire(eq("gateway:traffic:client-1:operations"), any(Duration.class))).thenReturn(Mono.just(true));
        RedisTrafficBudgetStore store = new RedisTrafficBudgetStore(template);

        TrafficBudgetDecision decision = store.consume("client-1:operations", 2).block();

        assertThat(decision.allowed()).isTrue();
        assertThat(decision.remainingRequests()).isEqualTo(1);
        verify(template).expire(eq("gateway:traffic:client-1:operations"), any(Duration.class));
    }

    @Test
    void shouldRejectRequestsAboveBudget() {
        ReactiveStringRedisTemplate template = mock(ReactiveStringRedisTemplate.class);
        ReactiveValueOperations<String, String> valueOperations = mock(ReactiveValueOperations.class);
        when(template.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("gateway:traffic:client-1:operations")).thenReturn(Mono.just(3L));
        RedisTrafficBudgetStore store = new RedisTrafficBudgetStore(template);

        TrafficBudgetDecision decision = store.consume("client-1:operations", 2).block();

        assertThat(decision.allowed()).isFalse();
        assertThat(decision.retryAfterSeconds()).isEqualTo(60);
    }
}
