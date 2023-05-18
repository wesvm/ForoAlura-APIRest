package com.foro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foro.api.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
}
