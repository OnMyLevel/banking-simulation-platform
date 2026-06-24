package com.banking.gateway.traffic;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.gateway")
public class GatewayTrafficProperties {
    private TrafficStoreMode trafficStoreMode = TrafficStoreMode.IN_MEMORY;
    private int usersBudgetPerMinute = 120;
    private int accountsBudgetPerMinute = 60;
    private int operationsBudgetPerMinute = 30;
    private int defaultBudgetPerMinute = 180;

    public TrafficStoreMode trafficStoreMode() {
        return trafficStoreMode;
    }

    public void setTrafficStoreMode(TrafficStoreMode trafficStoreMode) {
        this.trafficStoreMode = trafficStoreMode;
    }

    public int usersBudgetPerMinute() {
        return usersBudgetPerMinute;
    }

    public void setUsersBudgetPerMinute(int usersBudgetPerMinute) {
        this.usersBudgetPerMinute = usersBudgetPerMinute;
    }

    public int accountsBudgetPerMinute() {
        return accountsBudgetPerMinute;
    }

    public void setAccountsBudgetPerMinute(int accountsBudgetPerMinute) {
        this.accountsBudgetPerMinute = accountsBudgetPerMinute;
    }

    public int operationsBudgetPerMinute() {
        return operationsBudgetPerMinute;
    }

    public void setOperationsBudgetPerMinute(int operationsBudgetPerMinute) {
        this.operationsBudgetPerMinute = operationsBudgetPerMinute;
    }

    public int defaultBudgetPerMinute() {
        return defaultBudgetPerMinute;
    }

    public void setDefaultBudgetPerMinute(int defaultBudgetPerMinute) {
        this.defaultBudgetPerMinute = defaultBudgetPerMinute;
    }
}
