package com.foro.api.model.dto.auth;

public record AuthRequest(
        String email,
        String password
) {
}
