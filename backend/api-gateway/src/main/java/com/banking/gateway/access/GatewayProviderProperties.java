package com.banking.gateway.access;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.gateway.provider")
public class GatewayProviderProperties {
    private String name = "local-provider";
    private String issuer = "local-issuer";
    private String keysPath = "/keys";
    private String audience = "banking-api-gateway";

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String issuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String keysPath() {
        return keysPath;
    }

    public void setKeysPath(String keysPath) {
        this.keysPath = keysPath;
    }

    public String audience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }
}
