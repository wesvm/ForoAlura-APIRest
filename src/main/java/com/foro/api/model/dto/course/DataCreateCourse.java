package com.foro.api.model.dto.course;

import jakarta.validation.constraints.NotBlank;

public record DataCreateCourse(
        @NotBlank
        String name
) {
}
