package com.foro.api.model.dto.topic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foro.api.model.dto.course.DataResponseCourse;
import com.foro.api.model.dto.reply.DataListReplyTopic;
import com.foro.api.model.dto.user.DataResponseUser;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DataResponseReplyTopic(
        Long id, String title, String content, String creationDate, String modificationDate, String status,
        DataResponseUser author, DataResponseCourse course,
        List<DataListReplyTopic> replies
) {
}
