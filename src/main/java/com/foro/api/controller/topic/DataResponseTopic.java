package com.foro.api.controller.topic;

import com.foro.api.controller.course.DataResponseCourse;
import com.foro.api.controller.user.DataResponseUser;

public record DataResponseTopic(
        Long id, String title, String content, String creationDate, String status,
        DataResponseUser author, DataResponseCourse course) {

}
