package com.foro.api.record.error;
public record ValidationError(
        String field,
        String message
) {
}
