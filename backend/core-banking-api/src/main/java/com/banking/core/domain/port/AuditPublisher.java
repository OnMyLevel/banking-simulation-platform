package com.banking.core.domain.port;

import com.banking.core.domain.model.AuditEvent;

public interface AuditPublisher {
    void publish(AuditEvent event);
}
