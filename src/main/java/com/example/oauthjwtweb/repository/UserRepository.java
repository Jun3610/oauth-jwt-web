package com.example.oauthjwtweb.repository;

import com.example.oauthjwtweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { }
