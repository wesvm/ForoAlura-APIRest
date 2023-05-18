package com.foro.api.model.dto.course;

import com.foro.api.model.Course;

public record DataListCourse(Long id, String name) {
    public DataListCourse(Course course) {
        this(course.getId(), course.getName());
    }
}
