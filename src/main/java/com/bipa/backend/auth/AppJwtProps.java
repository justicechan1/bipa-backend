package com.bipa.backend.auth;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class AppJwtProps {
    private String secret;
    private int accessExpSeconds;
    private int refreshExpSeconds;

    public void setSecret(String s){ this.secret = s; }
    public void setAccessExpSeconds(int v){ this.accessExpSeconds = v; }
    public void setRefreshExpSeconds(int v){ this.refreshExpSeconds = v; }
}
