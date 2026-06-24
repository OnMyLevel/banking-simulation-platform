package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.stereotype.Component;

@Component
public class NoopEventSender implements EventSender {
    private static final String DESTINATION_TYPE = "NOOP";

    @Override
    public boolean supports(String destinationType) {
        return DESTINATION_TYPE.equals(destinationType);
    }

    @Override
    public void send(OutboxEvent event) {
        // Intentionally empty. Useful to disable delivery without changing domain code.
    }
}
