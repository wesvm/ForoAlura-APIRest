package com.foro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.foro.api.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
}
