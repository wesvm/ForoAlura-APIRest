package com.foro.api.controller.user;

import com.foro.api.models.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<DataListUser> getAllUsers(
            @PageableDefault(size = 20)
            Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);
        return users.map(DataListUser::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseUser> getUserById(
            @PathVariable Long id
    ){
        User user = userRepository.getReferenceById(id);
        var dataUser = new DataResponseUser(
                user.getId(), user.getName(), user.getEmail()
        );

        return ResponseEntity.ok(dataUser);
    }
}
