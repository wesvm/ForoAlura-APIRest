package com.foro.api.model.dto.reply;

import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.model.Reply;

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
