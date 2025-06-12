package com.example.ConcertTracker.repository;

import com.example.ConcertTracker.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface kakaoAuthRepository extends JpaRepository<User, String> {

    Optional<User> findByOauthId(String OAuth_id);
}
