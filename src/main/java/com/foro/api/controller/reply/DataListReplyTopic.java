package com.foro.api.controller.reply;

import com.foro.api.models.Reply.Reply;
import com.foro.api.controller.user.DataResponseUser;

public record DataListReplyTopic(
        Long id,
        String message,
        String creationDate,
        DataResponseUser author,
        Boolean solution
) {
    public DataListReplyTopic(Reply reply){
        this(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(),
                new DataResponseUser(
                        reply.getAuthor().getId(), reply.getAuthor().getName(), reply.getAuthor().getEmail()
                ), reply.getSolution()
        );
    }

}
