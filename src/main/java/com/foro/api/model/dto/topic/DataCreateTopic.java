package com.foro.api.model.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataCreateTopic(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotNull
        Long authorId,
        @NotNull
        Long courseId
) {
}
