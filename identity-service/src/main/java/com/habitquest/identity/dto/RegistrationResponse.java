package com.habitquest.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class RegistrationResponse {

    private final UUID id;
    private final String email;
    private final String displayName;

}
