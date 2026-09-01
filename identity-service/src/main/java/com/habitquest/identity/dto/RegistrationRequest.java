package com.habitquest.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    @Email
    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(min = 8, max = 72)
    @Pattern(
            regexp = "^(?=.*\\p{L})(?=.*[0-9]).+$",
            message = "Пароль должен содержать хотя бы одну букву и одну цифру"
    )
    private String password;

    @NotNull
    @Size(min = 2, max = 100)
    private String displayName;

}
