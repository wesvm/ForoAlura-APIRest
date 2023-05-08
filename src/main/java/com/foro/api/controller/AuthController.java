package com.foro.api.controller;

import com.foro.api.record.auth.DataAuthUser;
import com.foro.api.record.user.DataRegisterUser;
import com.foro.api.record.user.DataResponseUser;
import com.foro.api.models.User;
import com.foro.api.repository.UserRepository;
import com.foro.api.record.auth.DataJwtToken;
import com.foro.api.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> authUser(
            @RequestBody @Valid DataAuthUser dataAuthUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                dataAuthUser.email(), dataAuthUser.password());
        var authenticatedUser = authenticationManager.authenticate(authentication);
        var jwtToken = tokenService.generateToken((User) authenticatedUser.getPrincipal());

        return ResponseEntity.ok(new DataJwtToken(jwtToken));
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponseUser> saveUser(
            @RequestBody @Valid DataRegisterUser dataRegisterUser,
            UriComponentsBuilder uriComponentsBuilder) {
        User user = userRepository.save(new User(dataRegisterUser, passwordEncoder));
        var token = tokenService.generateToken(user);
        DataResponseUser dataResponseUser = new DataResponseUser(
                user.getId(), user.getName(), user.getEmail());
        URI url = uriComponentsBuilder.path("/register/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(url)
                .header("Authorization", "Bearer " + token)
                .body(dataResponseUser);
    }
}
