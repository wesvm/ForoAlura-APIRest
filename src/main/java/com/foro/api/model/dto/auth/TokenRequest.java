package com.foro.api.model.dto.auth;

import jakarta.validation.constraints.NotNull;

public record TokenRequest(
        @NotNull
        Long userId,
        @NotNull
        String token
) {
}
