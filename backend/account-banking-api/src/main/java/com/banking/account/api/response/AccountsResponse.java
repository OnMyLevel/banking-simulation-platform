package com.banking.account.api.response;

import java.util.List;

public record AccountsResponse(List<AccountResponse> items, int limit, int offset, int nextOffset) {
}
