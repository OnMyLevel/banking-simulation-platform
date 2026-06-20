package com.banking.account.domain.model;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
}
