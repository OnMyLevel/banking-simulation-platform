package com.banking.core.infrastructure.audit;

import com.banking.core.domain.model.AuditEvent;
import com.banking.core.domain.port.AuditPublisher;
import org.springframework.stereotype.Component;

@Component
public class NoOpAuditPublisher implements AuditPublisher {
    @Override
    public void publish(AuditEvent event) {
        // Intentionally empty until the observability integration is implemented.
    }
}
