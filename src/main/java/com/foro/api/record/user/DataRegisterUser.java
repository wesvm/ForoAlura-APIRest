package com.foro.api.record.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DataRegisterUser(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}
