package com.habitquest.identity.service;

import com.habitquest.identity.dto.RegistrationRequest;
import com.habitquest.identity.dto.RegistrationResponse;
import com.habitquest.identity.entity.User;
import com.habitquest.identity.entity.UserRole;
import com.habitquest.identity.entity.UserStatus;
import com.habitquest.identity.exception.EmailAlreadyException;
import com.habitquest.identity.mapper.UserMapper;
import com.habitquest.identity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        String email = request.getEmail().trim();
        String normalizedEmail = email.toLowerCase(Locale.ROOT);

        if (userRepository.existsByNormalizedEmail(normalizedEmail)) {
            throw new EmailAlreadyException();
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .normalizedEmail(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .userRole(UserRole.USER)
                .userStatus(UserStatus.ACTIVE)
                .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                .updatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        return userMapper.toRegistrationResponse(userRepository.save(user));
    }

}
