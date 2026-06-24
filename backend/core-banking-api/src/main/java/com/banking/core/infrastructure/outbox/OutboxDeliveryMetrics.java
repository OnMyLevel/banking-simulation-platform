package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;
import com.banking.core.domain.repository.OutboxEventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OutboxDeliveryMetrics {
    private final Counter successCounter;
    private final Counter failureCounter;

    public OutboxDeliveryMetrics(MeterRegistry meterRegistry, OutboxEventRepository repository) {
        this.successCounter = Counter.builder("banking.outbox.delivery.success")
            .description("Number of outbox events delivered successfully")
            .register(meterRegistry);
        this.failureCounter = Counter.builder("banking.outbox.delivery.failure")
            .description("Number of outbox event delivery failures")
            .register(meterRegistry);

        registerStatusGauge(meterRegistry, repository, OutboxEventStatus.PENDING);
        registerStatusGauge(meterRegistry, repository, OutboxEventStatus.FAILED);
        registerStatusGauge(meterRegistry, repository, OutboxEventStatus.SENT);
    }

    public void recordSuccess(OutboxEvent event) {
        successCounter.increment();
    }

    public void recordFailure(OutboxEvent event) {
        failureCounter.increment();
    }

    private void registerStatusGauge(MeterRegistry meterRegistry, OutboxEventRepository repository, OutboxEventStatus status) {
        Gauge.builder("banking.outbox.events", repository, currentRepository -> currentRepository.countByStatus(status))
            .description("Current number of outbox events by status")
            .tag("status", status.name())
            .register(meterRegistry);
    }
}
