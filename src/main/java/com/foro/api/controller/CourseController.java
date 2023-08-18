package com.foro.api.controller;

import com.foro.api.exception.ResourceNotFoundException;
import com.foro.api.model.Course;
import com.foro.api.model.dto.course.DataCreateCourse;
import com.foro.api.model.dto.course.DataListCourse;
import com.foro.api.model.dto.course.DataResponseCourse;
import com.foro.api.model.dto.course.DataUpdateCourse;
import com.foro.api.repository.CourseRepository;

import com.foro.api.service.CourseService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @GetMapping
    public Page<DataListCourse> getAllCourses(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Course> courses = courseRepository.findByStatusTrue(pageable);

        return courses.map(DataListCourse::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseCourse> getCourseById(
            @PathVariable Long id) {
        Course course = courseRepository.findByIdAndStatusTrue(id)
                .orElseThrow(()->new ResourceNotFoundException("unable to find requested resource"));
        var dataCourse = new DataResponseCourse(
                course.getId(), course.getName());

        return ResponseEntity.ok(dataCourse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponseCourse> createCourse(
            @Valid @RequestBody DataCreateCourse dataCreateCourse,
            UriComponentsBuilder uriComponentsBuilder){

        var course = courseService.create(dataCreateCourse);

        URI url = uriComponentsBuilder.path("/course/{id}").buildAndExpand(course.id()).toUri();
        return ResponseEntity.created(url).body(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Transactional
    public ResponseEntity<DataResponseCourse> updateCourse(
            @Valid @RequestBody DataUpdateCourse dataUpdateCourse){
        return ResponseEntity.ok().body(courseService.update(dataUpdateCourse));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id){
        courseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
