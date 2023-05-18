package com.foro.api.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DataRegisterUser(
        @NotBlank
        @Size(max = 50)
        String name,
        @NotBlank
        @Email
        @Size(max = 100)
        String email,
        @NotBlank
        String password
) {
}
