package com.banking.account.api.response;

public record MoneyResponse(
    String amount,
    String currency
) {}
