package com.foro.api.service;

import com.foro.api.model.Course;
import com.foro.api.model.dto.course.DataCreateCourse;
import com.foro.api.model.dto.course.DataResponseCourse;
import com.foro.api.model.dto.course.DataUpdateCourse;
import com.foro.api.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public DataResponseCourse create(DataCreateCourse dataCreateCourse){
        Course course = courseRepository.save(new Course(dataCreateCourse));
        return buildDataResponseCourse(course);
    }

    public DataResponseCourse update(DataUpdateCourse dataUpdateCourse){
        Course course = courseRepository.getReferenceById(dataUpdateCourse.id());

        course.updateCourse(dataUpdateCourse);

        return buildDataResponseCourse(course);
    }

    public void delete(Long id){
        Course course = courseRepository.getReferenceById(id);
        course.changeStatusCourse();
    }

    private DataResponseCourse buildDataResponseCourse(Course course) {
        return new DataResponseCourse(course.getId(), course.getName());
    }

}
