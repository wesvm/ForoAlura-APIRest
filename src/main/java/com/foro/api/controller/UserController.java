package com.foro.api.controller;

import com.foro.api.model.User;
import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

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
