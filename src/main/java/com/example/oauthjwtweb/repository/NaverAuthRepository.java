package com.example.oauthjwtweb.repository;

import com.example.oauthjwtweb.dto.NaverAuthDto.UserInfoFromKakakoByTokenDto;
import com.example.oauthjwtweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NaverAuthRepository extends JpaRepository {
    Optional <User> findByoauthId (String OAuth_id);
}