package com.example.oauthjwtweb.repository;

import com.example.oauthjwtweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String username);
}
