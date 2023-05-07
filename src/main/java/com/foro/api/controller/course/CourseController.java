package com.foro.api.controller.course;

import com.foro.api.models.Course.Course;
import com.foro.api.models.Course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

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
