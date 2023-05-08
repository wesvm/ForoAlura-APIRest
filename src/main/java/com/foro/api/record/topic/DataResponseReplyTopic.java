package com.foro.api.record.topic;

import com.foro.api.record.course.DataResponseCourse;
import com.foro.api.record.reply.DataListReplyTopic;
import com.foro.api.record.user.DataResponseUser;

import java.util.List;

public record DataResponseReplyTopic(
        Long id, String title, String content, String creationDate, String status,
        DataResponseUser author, DataResponseCourse course,
        List<DataListReplyTopic> replies
) {
}
