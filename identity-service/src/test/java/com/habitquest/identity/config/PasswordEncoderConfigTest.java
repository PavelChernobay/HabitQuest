package com.habitquest.identity.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderConfigTest {

    private static AnnotationConfigApplicationContext context;
    private static PasswordEncoder passwordEncoder;
    private String rawPassword;
    private String wrongPassword;

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(
                PasswordEncoderConfig.class);

        passwordEncoder = context.getBean(PasswordEncoder.class);
    }

    @BeforeEach
    void setUpPassword() {
        rawPassword = "Password1";
        wrongPassword = "WrongPassword1";
    }

    @AfterAll
    static void tearDown() {
        context.close();
    }

    @Test
    void passwordEncoderBeanShouldUserBCrypt() {
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void samePasswordShouldProduceDifferentHashes() {
        String firstHash = passwordEncoder.encode(rawPassword);
        String secondHash = passwordEncoder.encode(rawPassword);

        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordEncoder.matches(rawPassword,firstHash));
        assertTrue(passwordEncoder.matches(rawPassword,secondHash));
    }

    @Test
    void matchesShouldAcceptCorrectPassword() {
        String hash = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, hash));
    }

    @Test
    void matchesShouldRejectIncorrectPassword() {
        String hash = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches(wrongPassword, hash));
    }
}