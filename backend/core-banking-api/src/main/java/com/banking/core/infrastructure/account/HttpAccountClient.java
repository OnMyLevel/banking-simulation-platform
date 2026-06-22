package com.banking.core.infrastructure.account;

import com.banking.core.domain.exception.AccountServiceUnavailableException;
import com.banking.core.domain.exception.CoreAccountNotFoundException;
import com.banking.core.domain.model.AccountSnapshot;
import com.banking.core.domain.port.AccountClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class HttpAccountClient implements AccountClient {
    private final RestClient accountRestClient;

    public HttpAccountClient(RestClient accountRestClient) {
        this.accountRestClient = accountRestClient;
    }

    @Override
    public AccountSnapshot getAccount(UUID accountId) {
        try {
            AccountHttpResponse response = accountRestClient.get()
                .uri("/accounts/{accountId}", accountId)
                .retrieve()
                .onStatus(status -> HttpStatus.NOT_FOUND.equals(status), (request, clientResponse) -> {
                    throw new CoreAccountNotFoundException(accountId);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                    throw new AccountServiceUnavailableException(null);
                })
                .body(AccountHttpResponse.class);
            return new AccountSnapshot(response.id(), response.status());
        } catch (CoreAccountNotFoundException | AccountServiceUnavailableException ex) {
            throw ex;
        } catch (RestClientException ex) {
            throw new AccountServiceUnavailableException(ex);
        }
    }
}
