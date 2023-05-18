package com.foro.api.model.dto.user;

import com.foro.api.model.User;

public record DataListUser(Long id, String name, String email) {
    public DataListUser(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
