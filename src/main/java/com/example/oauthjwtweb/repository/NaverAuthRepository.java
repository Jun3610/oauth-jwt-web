package com.example.oauthjwtweb.repository;

import com.example.oauthjwtweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface NaverAuthRepository extends JpaRepository<User, String> {
    Optional <User> findByOauthId (String OAuth_id);
}