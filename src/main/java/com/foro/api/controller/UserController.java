package com.foro.api.controller;

import com.foro.api.models.User;
import com.foro.api.record.user.DataListUser;
import com.foro.api.record.user.DataResponseUser;
import com.foro.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public Page<DataListUser> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(DataListUser::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseUser> getUserById(
            @PathVariable Long id) {
        User user = userRepository.getReferenceById(id);
        var dataUser = new DataResponseUser(
                user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.ok(dataUser);
    }
}
