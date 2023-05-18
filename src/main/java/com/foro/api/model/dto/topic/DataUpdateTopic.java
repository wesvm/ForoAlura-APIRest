package com.foro.api.model.dto.topic;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record DataUpdateTopic(
        @NotNull
        Long id,
        String title,
        String content,
        String status,
        Optional<Long> courseId
) {
}
