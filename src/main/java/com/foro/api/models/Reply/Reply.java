package com.foro.api.models.Reply;

import com.foro.api.controller.reply.DataCreateReply;
import com.foro.api.controller.reply.DataUpdateReply;
import com.foro.api.models.Topic.Topic;
import com.foro.api.models.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "replies")
@Entity(name = "reply")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    private Boolean solution;

    public Reply(DataCreateReply dataCreateReply, User author, Topic topic){
        this.message = dataCreateReply.message();
        this.creationDate = LocalDateTime.now();
        this.author = author;
        this.topic = topic;
        this.solution = false;
    }

    public void updateReply(DataUpdateReply dataUpdateReply){
        if (dataUpdateReply.message() != null){
            this.message = dataUpdateReply.message();
            this.creationDate = LocalDateTime.now();
        }
    }

    public void changeSolutionReply(){
        this.solution = true;
    }

}
