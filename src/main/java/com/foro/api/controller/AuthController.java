package com.foro.api.controller;

import com.foro.api.model.dto.auth.AuthRequest;
import com.foro.api.model.dto.auth.AuthResponse;
import com.foro.api.model.dto.error.ApiError;
import com.foro.api.model.dto.user.DataRegisterUser;
import com.foro.api.model.dto.user.DataResponseCreatedUser;
import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    private final AuthService authService;

    @Operation(
            summary = "log in to the application",
            description = "logs a user into the application",
            security = @SecurityRequirement(name = "noAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class))
                    )
            }
    )
    @SecurityRequirements()
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.jwtToken()).body(response);
    }

    @Operation(
            summary = "register a new user",
            description = "registers a new user with the application",
            security = @SecurityRequirement(name = "noAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DataResponseUser.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict: email already taken",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)))
            }
    )
    @SecurityRequirements()
    @PostMapping("/register")
    public ResponseEntity<DataResponseCreatedUser> saveUser(
            @RequestBody @Valid DataRegisterUser request,
            UriComponentsBuilder uriComponentsBuilder) {

        DataResponseCreatedUser dataResponseUser = authService.register(request);

        URI url = uriComponentsBuilder.path("/register/{id}").buildAndExpand(dataResponseUser.id()).toUri();

        return ResponseEntity.created(url)
                .header("Authorization", "Bearer " + dataResponseUser.jwtToken())
                .body(dataResponseUser);
    }
}
