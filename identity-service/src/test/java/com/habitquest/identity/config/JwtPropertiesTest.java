package com.habitquest.identity.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtPropertiesTest {

    private JwtProperties properties;

    @BeforeEach
    void setUp() {
        properties = validProperties();
    }

    private JwtProperties validProperties() {
        JwtProperties result = new JwtProperties();

        result.setSecret("0123456789abcdef0123456789abcdef");
        result.setIssuer("habitquest-identity");
        result.setAccessTokenTtl(Duration.ofMinutes(15));
        return result;
    }

    @Test
    void validPropertiesShouldPassValidation() {
        assertDoesNotThrow(() -> validProperties().validate());
    }

    @Test
    void missingSecretShouldFailValidation() {
        properties.setSecret(null);

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void shortSecretShouldFailValidation() {
        properties.setSecret("avc");

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void wrongIssuerShouldFailValidation() {
        properties.setIssuer("wrong-issuer");

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void zeroTtlShouldFailValidation() {
        properties.setAccessTokenTtl(Duration.ZERO);

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void negativeTtlShouldFailValidation() {
        properties.setAccessTokenTtl(Duration.ofMinutes(-1));

        assertThrows(IllegalStateException.class, properties::validate);
    }

}
