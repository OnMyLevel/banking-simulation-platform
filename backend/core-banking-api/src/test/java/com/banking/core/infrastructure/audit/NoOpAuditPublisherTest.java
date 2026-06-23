package com.banking.core.infrastructure.audit;

import com.banking.core.domain.model.OutboxEvent;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.UUID;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class NoOpAuditPublisherTest {
    @Test
    void shouldSendOutboxPayloadToRemoteService() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://events-service");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAuditPublisher publisher = new HttpAuditPublisher(builder.build());
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{\"eventKind\":\"CREDIT\"}");

        server.expect(once(), requestTo("http://events-service/events"))
            .andExpect(method(POST))
            .andExpect(content().json("{\"eventKind\":\"CREDIT\"}"))
            .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        publisher.send(event);

        server.verify(Duration.ofSeconds(1));
    }
}
