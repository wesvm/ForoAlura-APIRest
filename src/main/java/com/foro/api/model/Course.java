package com.foro.api.model;

import com.foro.api.model.dto.course.DataCreateCourse;
import com.foro.api.model.dto.course.DataUpdateCourse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "courses")
@Entity(name = "course")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean status;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics = new ArrayList<>();

    public Course(DataCreateCourse dataCreateCourse){
        this.name = dataCreateCourse.name();
        this.status = true;
    }

    public void updateCourse(DataUpdateCourse dataUpdateCourse){
        if (dataUpdateCourse.name() != null && !dataUpdateCourse.name().trim().isEmpty()){
            this.name = dataUpdateCourse.name();
        }
    }

    public void changeStatusCourse(){
        this.status = false;
    }
}
