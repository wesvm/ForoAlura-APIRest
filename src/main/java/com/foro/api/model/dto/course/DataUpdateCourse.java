package com.foro.api.model.dto.course;

import jakarta.validation.constraints.NotNull;

public record DataUpdateCourse(
        @NotNull
        Long id,
        String name
){
}
