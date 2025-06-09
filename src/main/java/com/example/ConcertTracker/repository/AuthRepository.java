package com.example.ConcertTracker.repository;

import com.example.ConcertTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface AuthRepository extends JpaRepository<User, UUID> {}
