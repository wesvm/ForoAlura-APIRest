package com.foro.api.controller.course;

import com.foro.api.models.Course.Course;

public record DataListCourse(Long id, String name) {
    public DataListCourse(Course course){
        this(course.getId(), course.getName());
    }
}
