package com.banking.gateway.traffic;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableConfigurationProperties(GatewayTrafficProperties.class)
public class TrafficBudgetFilter implements WebFilter {
    private final GatewayTrafficProperties properties;
    private final Clock clock;
    private final Map<String, RequestBucket> buckets = new ConcurrentHashMap<>();

    public TrafficBudgetFilter(GatewayTrafficProperties properties) {
        this(properties, Clock.systemUTC());
    }

    TrafficBudgetFilter(GatewayTrafficProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        if (path.startsWith("/actuator/")) {
            return chain.filter(exchange);
        }

        String bucketKey = clientKey(exchange) + ":" + routeFamily(path);
        int budget = budgetFor(path);
        RequestBucket bucket = buckets.compute(bucketKey, (ignored, current) -> currentBucket(current));

        if (bucket.count().incrementAndGet() > budget) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().set("Retry-After", "60");
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private RequestBucket currentBucket(RequestBucket current) {
        Instant currentMinute = Instant.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (current == null || !current.minute().equals(currentMinute)) {
            return new RequestBucket(currentMinute, new AtomicInteger(0));
        }
        return current;
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

    private record RequestBucket(Instant minute, AtomicInteger count) {
    }
}
