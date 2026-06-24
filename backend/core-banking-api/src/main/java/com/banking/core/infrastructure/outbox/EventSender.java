package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;

public interface EventSender {
    boolean supports(String destinationType);
    void send(OutboxEvent event);
}
