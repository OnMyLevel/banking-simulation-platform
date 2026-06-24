package com.banking.gateway.traffic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@ConditionalOnProperty(prefix = "banking.gateway", name = "traffic-store-mode", havingValue = "REDIS")
public class RedisTrafficBudgetStore implements TrafficBudgetStore {
    private static final Duration WINDOW = Duration.ofMinutes(1);
    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisTrafficBudgetStore(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<TrafficBudgetDecision> consume(String bucketKey, int budget) {
        String key = "gateway:traffic:" + bucketKey;
        return redisTemplate.opsForValue().increment(key)
            .flatMap(consumed -> setExpiryIfFirstRequest(key, consumed)
                .thenReturn(toDecision(consumed, budget))
            );
    }

    private Mono<Boolean> setExpiryIfFirstRequest(String key, Long consumed) {
        if (consumed != null && consumed == 1L) {
            return redisTemplate.expire(key, WINDOW);
        }
        return Mono.just(Boolean.TRUE);
    }

    private TrafficBudgetDecision toDecision(Long consumed, int budget) {
        if (consumed == null) {
            return TrafficBudgetDecision.rejected(60);
        }
        if (consumed > budget) {
            return TrafficBudgetDecision.rejected(60);
        }
        return TrafficBudgetDecision.allowed(Math.max(0, budget - consumed));
    }
}
