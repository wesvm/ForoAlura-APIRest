package com.foro.api.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DataResponseCreatedUser(
        Long id,
        String name,
        String email,
        @JsonProperty("access_token") String jwtToken
) {
}
