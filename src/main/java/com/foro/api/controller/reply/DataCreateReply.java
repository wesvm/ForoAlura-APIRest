package com.foro.api.controller.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataCreateReply(
        @NotBlank
        String message,
        @NotNull
        Long authorId,
        @NotNull
        Long topicId
) {
}
