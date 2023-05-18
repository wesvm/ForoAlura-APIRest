package com.foro.api.model;

import com.foro.api.model.dto.reply.DataCreateReply;
import com.foro.api.model.dto.reply.DataUpdateReply;

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
    private LocalDateTime modificationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    private Boolean solution;

    public Reply(DataCreateReply dataCreateReply, User author, Topic topic) {
        this.message = dataCreateReply.message();
        this.creationDate = LocalDateTime.now();
        this.author = author;
        this.topic = topic;
        this.solution = false;
    }

    public void updateReply(DataUpdateReply dataUpdateReply) {
        if (dataUpdateReply.message() != null && !dataUpdateReply.message().trim().isEmpty()) {
            this.message = dataUpdateReply.message();
            this.modificationDate = LocalDateTime.now();
        }
    }

    public void changeSolutionReply() {
        this.solution = true;
    }

    public String getModificationDate() {
        if (modificationDate == null) {
            return "Not modified";
        }
        return modificationDate.toString();
    }
}
