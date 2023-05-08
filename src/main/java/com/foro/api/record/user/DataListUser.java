package com.foro.api.record.user;

import com.foro.api.models.User;

public record DataListUser(Long id, String name, String email) {
    public DataListUser(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
