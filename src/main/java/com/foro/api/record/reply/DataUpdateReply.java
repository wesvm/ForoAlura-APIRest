package com.foro.api.record.reply;

import jakarta.validation.constraints.NotNull;

public record DataUpdateReply(
        @NotNull
        Long id,
        String message
) {
}
