package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestEventSender implements EventSender {
    private static final String DESTINATION_TYPE = "OBSERVABILITY_HTTP";
    private final RestClient auditRestClient;

    public RestEventSender(RestClient auditRestClient) {
        this.auditRestClient = auditRestClient;
    }

    @Override
    public boolean supports(String destinationType) {
        return DESTINATION_TYPE.equals(destinationType);
    }

    @Override
    public void send(OutboxEvent event) {
        auditRestClient.post()
            .uri("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .body(event.payload())
            .retrieve()
            .toBodilessEntity();
    }
}
