package com.foro.api.controller.reply;

import com.foro.api.controller.user.DataResponseUser;

public record DataResponseReply(
                Long id, String message, String creationDate,
                DataResponseUser author, Boolean solution) {
}
