package com.banking.gateway.traffic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ConditionalOnProperty(prefix = "banking.gateway", name = "traffic-store-mode", havingValue = "IN_MEMORY", matchIfMissing = true)
public class LocalTrafficBudgetStore implements TrafficBudgetStore {
    private final Clock clock;
    private final Map<String, RequestBucket> buckets = new ConcurrentHashMap<>();

    public LocalTrafficBudgetStore() {
        this(Clock.systemUTC());
    }

    LocalTrafficBudgetStore(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Mono<TrafficBudgetDecision> consume(String bucketKey, int budget) {
        RequestBucket bucket = buckets.compute(bucketKey, (ignored, current) -> currentBucket(current));
        int consumed = bucket.count().incrementAndGet();
        if (consumed > budget) {
            return Mono.just(TrafficBudgetDecision.rejected(60));
        }
        return Mono.just(TrafficBudgetDecision.allowed(Math.max(0, budget - consumed)));
    }

    private RequestBucket currentBucket(RequestBucket current) {
        Instant currentMinute = Instant.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (current == null || !current.minute().equals(currentMinute)) {
            return new RequestBucket(currentMinute, new AtomicInteger(0));
        }
        return current;
    }

    private record RequestBucket(Instant minute, AtomicInteger count) {
    }
}
