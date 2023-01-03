package com.chekanova.imagetool.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties("spring.security.access.token")
public class AccessTokenProperties {
    private String secret;
    private String issuer;
    private Duration tokenExpiration;
}
