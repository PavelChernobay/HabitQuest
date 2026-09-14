package com.habitquest.identity.service;

import com.habitquest.identity.dto.RegistrationRequest;
import com.habitquest.identity.dto.RegistrationResponse;
import com.habitquest.identity.entity.User;
import com.habitquest.identity.entity.UserRole;
import com.habitquest.identity.entity.UserStatus;
import com.habitquest.identity.exception.EmailAlreadyException;
import com.habitquest.identity.mapper.UserMapper;
import com.habitquest.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    private RegistrationRequest request;
    private String normalizedEmail;

    @BeforeEach
    void serUp() {
        request = RegistrationRequest.builder()
                .email("Test@gmail.com")
                .password("Password1")
                .displayName("Test")
                .build();
        normalizedEmail = "test@gmail.com";
    }

    @Test
    void registerShouldCreateUser() {
        String encodedPassword = "encoded-password";

        RegistrationResponse expectedResponse = RegistrationResponse.builder()
                .id(UUID.randomUUID())
                .email("Test@gmail.com")
                .displayName("Test")
                .build();

        when(userRepository.existsByNormalizedEmail(normalizedEmail)).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toRegistrationResponse(any(User.class))).thenReturn(expectedResponse);

        RegistrationResponse actualResponse = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertAll(
                () -> assertNotNull(savedUser.getId()),
                () -> assertEquals(request.getEmail(), savedUser.getEmail()),
                () -> assertEquals(normalizedEmail, savedUser.getNormalizedEmail()),
                () -> assertNotEquals(request.getPassword(), savedUser.getPasswordHash()),
                () -> assertEquals(request.getDisplayName(), savedUser.getDisplayName()),
                () -> assertEquals(UserRole.USER, savedUser.getUserRole()),
                () -> assertEquals(UserStatus.ACTIVE, savedUser.getUserStatus()),
                () -> assertNotNull(savedUser.getCreatedAt()),
                () -> assertNotNull(savedUser.getUpdatedAt()),
                () -> assertSame(expectedResponse, actualResponse)
        );

        verify(passwordEncoder).encode(request.getPassword());
        verify(userMapper).toRegistrationResponse(savedUser);
    }

    @Test
    void registerShouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByNormalizedEmail(normalizedEmail)).thenReturn(true);

        assertThrows(EmailAlreadyException.class, () -> authService.register(request));

        verify(userRepository).existsByNormalizedEmail(normalizedEmail);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder, userMapper);
    }

}