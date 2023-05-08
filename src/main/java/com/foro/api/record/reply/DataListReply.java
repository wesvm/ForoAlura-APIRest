package com.foro.api.record.reply;

import com.foro.api.record.course.DataResponseCourse;
import com.foro.api.record.topic.DataResponseTopic;
import com.foro.api.record.user.DataResponseUser;
import com.foro.api.models.Reply;

public record DataListReply(
        Long id,
        String message,
        String creationDate,
        DataResponseUser dataResponseUser,
        DataResponseTopic dataResponseTopic) {
    public DataListReply(Reply reply) {
        this(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(),
                new DataResponseUser(
                        reply.getAuthor().getId(), reply.getAuthor().getName(), reply.getAuthor().getEmail()),
                new DataResponseTopic(
                        reply.getTopic().getId(), reply.getTopic().getTitle(),
                        reply.getTopic().getContent(), reply.getCreationDate().toString(),
                        reply.getTopic().getTopicStatus().toString(),
                        new DataResponseUser(
                                reply.getTopic().getAuthor().getId(), reply.getTopic().getAuthor().getName(),
                                reply.getTopic().getAuthor().getEmail()),
                        new DataResponseCourse(
                                reply.getTopic().getCourse().getId(), reply.getTopic().getCourse().getName())));
    }
}
