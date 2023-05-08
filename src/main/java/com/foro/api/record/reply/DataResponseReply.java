package com.foro.api.record.reply;

import com.foro.api.record.user.DataResponseUser;

public record DataResponseReply(
                Long id, String message, String creationDate,
                DataResponseUser author, Boolean solution) {
}
