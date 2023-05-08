package com.foro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.api.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
