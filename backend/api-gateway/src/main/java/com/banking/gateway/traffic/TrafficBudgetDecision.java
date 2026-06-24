package com.banking.gateway.traffic;

public record TrafficBudgetDecision(boolean allowed, long remainingRequests, long retryAfterSeconds) {
    public static TrafficBudgetDecision allowed(long remainingRequests) {
        return new TrafficBudgetDecision(true, remainingRequests, 0);
    }

    public static TrafficBudgetDecision rejected(long retryAfterSeconds) {
        return new TrafficBudgetDecision(false, 0, retryAfterSeconds);
    }
}
