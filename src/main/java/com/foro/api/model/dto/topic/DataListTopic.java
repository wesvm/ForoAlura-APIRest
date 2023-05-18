package com.foro.api.model.dto.topic;

import com.foro.api.model.dto.course.DataResponseCourse;
import com.foro.api.model.Topic;
import com.foro.api.model.dto.user.DataResponseUser;

public record DataListTopic(
        Long id, String title, String content, String creationDate, String modificationDate,
        String status, DataResponseUser author, DataResponseCourse course) {

    public DataListTopic(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getContent(), topic.getCreationDate().toString(),
                topic.getModificationDate(), topic.getTopicStatus().toString(),
                new DataResponseUser(
                        topic.getAuthor().getId(), topic.getAuthor().getName(), topic.getAuthor().getEmail()),
                new DataResponseCourse(topic.getCourse().getId(), topic.getCourse().getName())
        );
    }
}
