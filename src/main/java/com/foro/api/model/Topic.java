package com.foro.api.model;

import com.foro.api.model.dto.topic.DataCreateTopic;

import com.foro.api.model.dto.topic.DataUpdateTopic;
import com.foro.api.model.enums.TopicStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topics")
@Entity(name = "topic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    @Enumerated(EnumType.STRING)
    private TopicStatus topicStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    public Topic(DataCreateTopic dataCreateTopic, User author, Course course) {
        this.title = dataCreateTopic.title();
        this.content = dataCreateTopic.content();
        this.creationDate = LocalDateTime.now();
        this.topicStatus = TopicStatus.NOT_ANSWERED;
        this.author = author;
        this.course = course;
    }

    public void updateTopic(DataUpdateTopic dataUpdateTopic, Course course){
        boolean isUpdated = false;

        if (dataUpdateTopic.title() != null && !dataUpdateTopic.title().trim().isEmpty()){
            this.title = dataUpdateTopic.title();
            isUpdated = true;
        }
        if (dataUpdateTopic.content() != null && !dataUpdateTopic.content().trim().isEmpty()){
            this.content = dataUpdateTopic.content();
            isUpdated = true;
        }
        if (dataUpdateTopic.courseId().isPresent()){
            this.course = course;
            isUpdated = true;
        }
        if (dataUpdateTopic.status() != null){
            this.topicStatus = TopicStatus.valueOf(dataUpdateTopic.status());
            isUpdated = true;
        }
        if (isUpdated) {
            this.modificationDate = LocalDateTime.now();
        }
    }

    public void setTopicStatusNotSolved() {
        this.topicStatus = TopicStatus.NOT_SOLVED;
    }

    public String getModificationDate() {
        if (modificationDate == null) {
            return "Not modified";
        }
        return modificationDate.toString();
    }

    public Boolean isClosed(){
        return topicStatus == TopicStatus.CLOSED || topicStatus == TopicStatus.SOLVED;
    }

}
