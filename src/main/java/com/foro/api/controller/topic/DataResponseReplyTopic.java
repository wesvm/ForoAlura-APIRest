package com.foro.api.controller.topic;

import com.foro.api.controller.course.DataResponseCourse;
import com.foro.api.controller.reply.DataListReplyTopic;
import com.foro.api.controller.user.DataResponseUser;

import java.util.List;

public record DataResponseReplyTopic(
        Long id, String title, String content, String creationDate, String status,
        DataResponseUser author, DataResponseCourse course,
        List<DataListReplyTopic> replies
) {
}
