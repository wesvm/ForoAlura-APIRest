package com.foro.api.record.topic;

import com.foro.api.record.course.DataResponseCourse;
import com.foro.api.record.user.DataResponseUser;

public record DataResponseTopic(
        Long id, String title, String content, String creationDate, String status,
        DataResponseUser author, DataResponseCourse course) {

}
