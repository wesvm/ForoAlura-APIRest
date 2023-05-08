package com.foro.api.record.topic;

import com.foro.api.record.course.DataResponseCourse;
import com.foro.api.models.Topic;
import com.foro.api.record.user.DataResponseUser;

public record DataListTopic(
        Long id, String title, String content, String creationDate,
        String status, DataResponseUser author, DataResponseCourse course) {

    public DataListTopic(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getContent(), topic.getCreationDate().toString(),
                topic.getTopicStatus().toString(),
                new DataResponseUser(
                        topic.getAuthor().getId(), topic.getAuthor().getName(), topic.getAuthor().getEmail()),
                new DataResponseCourse(topic.getCourse().getId(), topic.getCourse().getName())
        );
    }
}
