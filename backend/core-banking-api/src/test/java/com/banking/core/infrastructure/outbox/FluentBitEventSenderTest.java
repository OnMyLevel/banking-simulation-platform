package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class FluentBitEventSenderTest {
    @Test
    void shouldSupportFluentBitDestination() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://log-forwarder");
        FluentBitEventSender sender = new FluentBitEventSender(builder.build());

        assertThat(sender.supports("FLUENT_BIT")).isTrue();
        assertThat(sender.supports("OBSERVABILITY_HTTP")).isFalse();
    }

    @Test
    void shouldSendPayloadToLogForwarder() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://log-forwarder");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        FluentBitEventSender sender = new FluentBitEventSender(builder.build());
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "FLUENT_BIT", "{\"eventKind\":\"CREDIT\"}");

        server.expect(once(), requestTo("http://log-forwarder/banking.core"))
            .andExpect(method(POST))
            .andExpect(content().json("{\"eventKind\":\"CREDIT\"}"))
            .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        sender.send(event);

        server.verify(Duration.ofSeconds(1));
    }
}
