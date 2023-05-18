package com.foro.api.service;

import com.foro.api.exception.DuplicateResourceException;
import com.foro.api.model.User;
import com.foro.api.model.dto.auth.AuthRequest;
import com.foro.api.model.dto.auth.AuthResponse;
import com.foro.api.model.dto.user.DataRegisterUser;
import com.foro.api.model.dto.user.DataResponseCreatedUser;
import com.foro.api.repository.UserRepository;
import com.foro.api.security.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        User principal = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(principal);

        return new AuthResponse(token);
    }

    public DataResponseCreatedUser register(DataRegisterUser request){
        String email = request.email();
        if (userRepository.existsUserByEmail(email)){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        User user = userRepository.save(new User(request, passwordEncoder));
        var token = tokenService.generateToken(user);

        return new DataResponseCreatedUser(user.getId(), user.getName(), user.getEmail(), token);
    }

}
