package com.foro.api.model.dto.reply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foro.api.model.dto.user.DataResponseUser;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DataResponseReply(
        Long id, String message, String creationDate, String modificationDate,
        DataResponseUser author, Boolean solution, Long topic) {
}
