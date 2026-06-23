package com.banking.core.infrastructure.audit;

import com.banking.core.domain.model.AuditEvent;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class HttpAuditPublisherTest {
    @Test
    void shouldSendEventToRemoteService() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://events-service");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAuditPublisher publisher = new HttpAuditPublisher(builder.build());
        Operation operation = Operation.credit(UUID.randomUUID(), Money.of(BigDecimal.TEN, "EUR"), "key-1");

        server.expect(once(), requestTo("http://events-service/events"))
            .andExpect(method(POST))
            .andExpect(jsonPath("$.eventId").value(operation.id().toString()))
            .andExpect(jsonPath("$.eventKind").value("CREDIT"))
            .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        publisher.publish(AuditEvent.from(operation));

        server.verify(Duration.ofSeconds(1));
    }

    @Test
    void shouldNotFailWhenRemoteServiceFails() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://events-service");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAuditPublisher publisher = new HttpAuditPublisher(builder.build());
        Operation operation = Operation.credit(UUID.randomUUID(), Money.of(BigDecimal.TEN, "EUR"), "key-2");

        server.expect(once(), requestTo("http://events-service/events"))
            .andExpect(method(POST))
            .andRespond(withServerError());

        publisher.publish(AuditEvent.from(operation));

        server.verify(Duration.ofSeconds(1));
    }
}
