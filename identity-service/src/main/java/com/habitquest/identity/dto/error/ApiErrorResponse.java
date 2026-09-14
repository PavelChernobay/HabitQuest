package com.habitquest.identity.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApiErrorResponse {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String code;
    private final String message;
    private final String path;

}
