package com.foro.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class TopicClosedException extends RuntimeException {
    public TopicClosedException(String message){super(message);}
}
