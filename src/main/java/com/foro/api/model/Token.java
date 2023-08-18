package com.foro.api.model;

import com.foro.api.model.dto.auth.TokenRequest;
import com.foro.api.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
@Entity(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public boolean revoked;
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    public Token(TokenRequest tokenRequest, User user) {
        this.token = tokenRequest.token();
        this.tokenType = TokenType.BEARER;
        this.revoked = false;
        this.expired = false;
        this.user = user;
    }
}
