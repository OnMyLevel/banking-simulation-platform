package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FluentBitEventSender implements EventSender {
    private static final String DESTINATION_TYPE = "FLUENT_BIT";
    private final RestClient restClient;

    public FluentBitEventSender(@Qualifier("logForwarderRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean supports(String destinationType) {
        return DESTINATION_TYPE.equals(destinationType);
    }

    @Override
    public void send(OutboxEvent event) {
        restClient.post()
            .uri("/banking.core")
            .contentType(MediaType.APPLICATION_JSON)
            .body(event.payload())
            .retrieve()
            .toBodilessEntity();
    }
}
