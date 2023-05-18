package com.foro.api.model.dto.error;
public record ValidationError(
        String field,
        String message
) {
}
