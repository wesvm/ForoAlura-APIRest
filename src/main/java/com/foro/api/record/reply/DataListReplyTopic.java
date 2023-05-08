package com.foro.api.record.reply;

import com.foro.api.record.user.DataResponseUser;
import com.foro.api.models.Reply;

public record DataListReplyTopic(
        Long id,
        String message,
        String creationDate,
        DataResponseUser author,
        Boolean solution) {
    public DataListReplyTopic(Reply reply) {
        this(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(),
                new DataResponseUser(
                        reply.getAuthor().getId(), reply.getAuthor().getName(), reply.getAuthor().getEmail()),
                reply.getSolution());
    }

}
