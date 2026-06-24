package com.banking.gateway.traffic;

import reactor.core.publisher.Mono;

public interface TrafficBudgetStore {
    Mono<TrafficBudgetDecision> consume(String bucketKey, int budget);
}
