package com.foro.api.model.dto.reply;

import jakarta.validation.constraints.NotNull;

public record DataUpdateReply(
        @NotNull
        Long id,
        String message
) {
}
