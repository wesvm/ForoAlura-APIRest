package com.foro.api.record.auth;

public record AuthRequest(
        String email,
        String password
) {
}
