package com.banking.core.infrastructure.account;

import com.banking.core.domain.exception.AccountServiceUnavailableException;
import com.banking.core.domain.exception.CoreAccountNotFoundException;
import com.banking.core.domain.model.AccountSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class HttpAccountClientTest {

    @Test
    void shouldReturnAccountSnapshot() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://remote-account");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAccountClient client = new HttpAccountClient(builder.build());
        UUID accountId = UUID.randomUUID();

        server.expect(requestTo("http://remote-account/accounts/" + accountId))
            .andExpect(method(GET))
            .andRespond(withSuccess("{\"id\":\"" + accountId + "\",\"status\":\"ACTIVE\"}", APPLICATION_JSON));

        AccountSnapshot account = client.getAccount(accountId);

        assertThat(account.id()).isEqualTo(accountId);
        assertThat(account.isActive()).isTrue();
        server.verify();
    }

    @Test
    void shouldMapMissingAccount() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://remote-account");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAccountClient client = new HttpAccountClient(builder.build());
        UUID accountId = UUID.randomUUID();

        server.expect(requestTo("http://remote-account/accounts/" + accountId))
            .andExpect(method(GET))
            .andRespond(withStatus(NOT_FOUND));

        assertThatThrownBy(() -> client.getAccount(accountId))
            .isInstanceOf(CoreAccountNotFoundException.class);
        server.verify();
    }

    @Test
    void shouldMapRemoteError() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://remote-account");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        HttpAccountClient client = new HttpAccountClient(builder.build());
        UUID accountId = UUID.randomUUID();

        server.expect(requestTo("http://remote-account/accounts/" + accountId))
            .andExpect(method(GET))
            .andRespond(withStatus(INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> client.getAccount(accountId))
            .isInstanceOf(AccountServiceUnavailableException.class);
        server.verify();
    }
}
