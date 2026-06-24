package com.banking.gateway.traffic;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Component
@EnableConfigurationProperties(GatewayTrafficProperties.class)
public class TrafficBudgetFilter implements WebFilter {
    private final GatewayTrafficProperties properties;
    private final TrafficBudgetStore trafficBudgetStore;

    public TrafficBudgetFilter(GatewayTrafficProperties properties, TrafficBudgetStore trafficBudgetStore) {
        this.properties = properties;
        this.trafficBudgetStore = trafficBudgetStore;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        if (path.startsWith("/actuator/")) {
            return chain.filter(exchange);
        }

        String bucketKey = clientKey(exchange) + ":" + routeFamily(path);
        int budget = budgetFor(path);

        return trafficBudgetStore.consume(bucketKey, budget)
            .flatMap(decision -> handleDecision(exchange, chain, decision));
    }

    private Mono<Void> handleDecision(ServerWebExchange exchange, WebFilterChain chain, TrafficBudgetDecision decision) {
        if (decision.allowed()) {
            exchange.getResponse().getHeaders().set("X-RateLimit-Remaining", String.valueOf(decision.remainingRequests()));
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        exchange.getResponse().getHeaders().set("Retry-After", String.valueOf(decision.retryAfterSeconds()));
        return exchange.getResponse().setComplete();
    }

    private String clientKey(ServerWebExchange exchange) {
        String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        if (remoteAddress == null || remoteAddress.getAddress() == null) {
            return "unknown";
        }
        return remoteAddress.getAddress().getHostAddress();
    }

    private String routeFamily(String path) {
        if (path.startsWith("/api/users/")) {
            return "users";
        }
        if (path.startsWith("/api/accounts/")) {
            return "accounts";
        }
        if (path.startsWith("/api/operations/")) {
            return "operations";
        }
        return "default";
    }

    private int budgetFor(String path) {
        if (path.startsWith("/api/users/")) {
            return properties.usersBudgetPerMinute();
        }
        if (path.startsWith("/api/accounts/")) {
            return properties.accountsBudgetPerMinute();
        }
        if (path.startsWith("/api/operations/")) {
            return properties.operationsBudgetPerMinute();
        }
        return properties.defaultBudgetPerMinute();
    }
}
