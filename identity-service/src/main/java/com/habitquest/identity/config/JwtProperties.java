package com.habitquest.identity.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private String issuer;
    private Duration accessTokenTtl;

    @PostConstruct
    void validate() {
        if (secret == null
                || secret.isBlank()
                || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET должен содержать не менее 32 байт");
        }

        if (!"habitquest-identity".equals(issuer)) {
            throw new IllegalStateException("Неверный JWT issuer");
        }

        if (accessTokenTtl == null
        || accessTokenTtl.isZero()
        || accessTokenTtl.isNegative()) {
            throw new IllegalStateException("ACCESS_TOKEN_TTL должен быть больше нуля");
        }
    }

}
