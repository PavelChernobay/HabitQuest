package com.habitquest.identity.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private RegistrationRequest request;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUpRequest() {
        request = RegistrationRequest.builder()
                .email("test@example.com")
                .password("Password1")
                .displayName("Test")
                .build();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void validRequestShouldHaveNoViolations() {
        Set<ConstraintViolation<RegistrationRequest>> violations = getValidateRequest();

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "incorrect-email"})
    void invalidEmailShouldBeRejected(String email) {
        request.setEmail(email);

        Set<ConstraintViolation<RegistrationRequest>> violations = getValidateRequest();

        assertTrue(hasViolationForField(violations, "email"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abcdefgh",
            "12345678",
            "Abc123"
    })
    void invalidPasswordShouldBeRejected(String password) {
        request.setPassword(password);

        Set<ConstraintViolation<RegistrationRequest>> violations = getValidateRequest();

        assertTrue(hasViolationForField(violations, "password"));
    }

    @Test
    void tooLongPasswordShouldBeRejected() {
        request.setPassword("A1" + "a".repeat(71));

        Set<ConstraintViolation<RegistrationRequest>> violations = getValidateRequest();

        assertTrue(hasViolationForField(violations, "password"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "P"})
    void invalidDisplayNameShouldBeRejected(String displayName) {
        request.setDisplayName(displayName);

        Set<ConstraintViolation<RegistrationRequest>> violations =
                getValidateRequest();

        assertTrue(hasViolationForField(violations, "displayName"));
    }

    private Set<ConstraintViolation<RegistrationRequest>> getValidateRequest() {
        return validator.validate(request);
    }

    private boolean hasViolationForField(Set<ConstraintViolation<RegistrationRequest>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals(fieldName));
    }

}