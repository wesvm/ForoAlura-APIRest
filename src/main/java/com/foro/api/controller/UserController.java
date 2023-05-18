package com.foro.api.controller;

import com.foro.api.model.User;
import com.foro.api.model.dto.user.DataListUser;
import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "gets a list of all users",
            tags = {"user"}
    )
    @GetMapping
    public Page<DataListUser> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(DataListUser::new);
    }

    @Operation(
            summary = "gets a user by id",
            tags = {"user"}
    )
    @GetMapping("/{id}")
    public ResponseEntity<DataResponseUser> getUserById(
            @PathVariable Long id) {
        User user = userRepository.getReferenceById(id);
        var dataUser = new DataResponseUser(
                user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.ok(dataUser);
    }
}
