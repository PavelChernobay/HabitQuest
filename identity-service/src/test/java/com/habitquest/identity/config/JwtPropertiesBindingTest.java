package com.habitquest.identity.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtPropertiesBindingTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withInitializer(
                            new ConfigDataApplicationContextInitializer()
                    )
                    .withUserConfiguration(TestConfig.class);

    @Test
    void applicationYamalShouldBindJwtProperties() {
        contextRunner
                .withPropertyValues(
                        "JWT_SECRET=0123456789abcdef0123456789abcdef",
                        "ACCESS_TOKEN_TTL=20m"
                )
                .run(context -> {
                    assertThat(context).hasNotFailed();

                    JwtProperties jwt = context.getBean(JwtProperties.class);

                    assertThat(jwt.getIssuer()).isEqualTo("habitquest-identity");

                    assertThat(jwt.getAccessTokenTtl()).isEqualTo(Duration.ofMinutes(20));
                });
    }

    @Test
    void shortSecretShouldPreventContextStart() {
        contextRunner
                .withPropertyValues("JWT_SECRET=abc")
                .run(context -> assertThat(context).hasFailed());
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(JwtProperties.class)
    static class TestConfig {

    }
}
