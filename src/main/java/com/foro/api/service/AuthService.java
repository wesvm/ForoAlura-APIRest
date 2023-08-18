package com.foro.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foro.api.exception.DuplicateResourceException;
import com.foro.api.model.Token;
import com.foro.api.model.User;
import com.foro.api.model.dto.auth.AuthRequest;
import com.foro.api.model.dto.auth.AuthResponse;
import com.foro.api.model.dto.auth.TokenRequest;
import com.foro.api.model.dto.user.DataRegisterUser;
import com.foro.api.repository.TokenRepository;
import com.foro.api.repository.UserRepository;
import com.foro.api.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        User principal = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);

        revokeAllUserTokens(principal);
        saveUserToken(new TokenRequest(principal.getId(), accessToken));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse register(DataRegisterUser request){
        String email = request.email();
        if (userRepository.existsUserByEmail(email)){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        User user = userRepository.save(new User(request, passwordEncoder));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(new TokenRequest(user.getId(), token));

        return new AuthResponse(token, refreshToken);
    }

    private void saveUserToken(TokenRequest request) {
        User user = userRepository.getReferenceById(request.userId());
        tokenRepository.save(new Token(request, user));
    }
    
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                var toquenRequest = new TokenRequest(user.getId(), accessToken);
                saveUserToken(toquenRequest);
                var authResponse =  new AuthResponse(accessToken, refreshToken);

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
