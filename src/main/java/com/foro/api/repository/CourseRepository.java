package com.foro.api.repository;

import com.foro.api.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusTrue(Pageable pageable);
    Optional<Course> findByIdAndStatusTrue(Long Long);
}
