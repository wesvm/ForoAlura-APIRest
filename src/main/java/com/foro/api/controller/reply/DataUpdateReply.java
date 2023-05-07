package com.foro.api.controller.reply;

import jakarta.validation.constraints.NotNull;

public record DataUpdateReply(
        @NotNull
        Long id,
        String message
) {
}
