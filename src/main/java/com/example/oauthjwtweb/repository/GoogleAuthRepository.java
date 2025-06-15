package com.example.oauthjwtweb.repository;

import com.example.oauthjwtweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleAuthRepository extends JpaRepository<User, String> {

}

