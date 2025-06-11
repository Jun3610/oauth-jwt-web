package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table (name = "user")
public class User {

    @Id
    private UUID user_id;

    @Column(nullable = false)
    private String oAuthId;

    private String email;

    private String user_name;

    private String provider;

    private LocalDateTime created_at;

    public User(UUID user_id, String oAuthId, String email, String user_name, String provider, LocalDateTime created_at) {
        this.user_id = user_id;
        this.oAuthId = oAuthId;
        this.email = email;
        this.user_name = user_name;
        this.provider = provider;
        this.created_at = created_at;
    }
}
