package com.banking.account.domain.service;

import java.util.UUID;

public class IbanGenerator {

    public String generate() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 23).toUpperCase();
        return "FR76" + suffix;
    }
}
