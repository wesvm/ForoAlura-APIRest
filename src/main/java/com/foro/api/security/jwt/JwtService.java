package com.foro.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${api.security.jwt.expiration}")
    private long JWT_EXPIRATION;
    @Value("${api.security.jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION;

    public String extractUsername(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        final DecodedJWT decodedJWT = extractAllClaims(token);
        return claimsResolver.apply(decodedJWT);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, REFRESH_EXPIRATION);
    }

    private String buildToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration){
        return JWT.create()
                //.withClaim("extraClaims",extractClaims)
                .withSubject(userDetails.getUsername())
                .withIssuer("FORO-ALURA")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getSignInKey());
    }

    private Algorithm getSignInKey() {
        return Algorithm.HMAC256(SECRET_KEY);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

    private DecodedJWT extractAllClaims(String token) {
        return JWT.require(getSignInKey())
                .build()
                .verify(token);
    }
}
