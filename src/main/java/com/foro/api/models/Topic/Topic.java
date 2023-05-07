package com.foro.api.models.Topic;

import com.foro.api.controller.topic.DataCreateTopic;
import com.foro.api.models.Course.Course;
import com.foro.api.models.Reply.Reply;
import com.foro.api.models.User.User;
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

    public Topic(DataCreateTopic dataCreateTopic, User author, Course course ){
        this.title = dataCreateTopic.title();
        this.content = dataCreateTopic.content();
        this.creationDate = LocalDateTime.now();
        this.topicStatus = TopicStatus.NOT_ANSWERED;
        this.author = author;
        this.course = course;
    }

    public void setTopicStatusNotSolved(){
        this.topicStatus = TopicStatus.NOT_SOLVED;
    }

}
