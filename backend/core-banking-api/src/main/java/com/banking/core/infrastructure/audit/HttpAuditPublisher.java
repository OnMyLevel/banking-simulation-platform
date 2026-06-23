package com.banking.core.infrastructure.audit;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpAuditPublisher {
    private final RestClient auditRestClient;

    public HttpAuditPublisher(RestClient auditRestClient) {
        this.auditRestClient = auditRestClient;
    }

    public void send(OutboxEvent event) {
        auditRestClient.post()
            .uri("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .body(event.payload())
            .retrieve()
            .toBodilessEntity();
    }
}
