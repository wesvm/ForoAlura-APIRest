package com.foro.api.controller;

import com.foro.api.models.Course;
import com.foro.api.record.course.DataListCourse;
import com.foro.api.record.course.DataResponseCourse;
import com.foro.api.repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;

    @GetMapping
    public Page<DataListCourse> getAllCourses(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);

        System.out.println(courses);
        return courses.map(DataListCourse::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseCourse> getCourseById(
            @PathVariable Long id) {
        Course course = courseRepository.getReferenceById(id);
        var dataCourse = new DataResponseCourse(
                course.getId(), course.getName());

        return ResponseEntity.ok(dataCourse);
    }

}
