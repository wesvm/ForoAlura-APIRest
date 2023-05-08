package com.foro.api.record.course;

import com.foro.api.models.Course;

public record DataListCourse(Long id, String name) {
    public DataListCourse(Course course) {
        this(course.getId(), course.getName());
    }
}
