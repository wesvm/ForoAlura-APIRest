package com.foro.api.controller.user;

import com.foro.api.models.User.User;

public record DataListUser(Long id, String name, String email) {
    public DataListUser(User user){
        this(user.getId(), user.getName(), user.getEmail());
    }
}
