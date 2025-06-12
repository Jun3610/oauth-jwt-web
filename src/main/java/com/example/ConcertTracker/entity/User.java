package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table (name = "user")
public class User {

    public User() {}

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String user_id;

    @Column(name = "OAuth_id")
    private String oauthId;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String user_name;

    @Column(name = "profile_image")
    private String profile_image;

    @Column(name = "provider")
    private String provider;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    public User(String user_id,
                String oauthId,
                String user_name,
                String profile_image,
                String provider,
                LocalDateTime created_at) {
                this.user_id = user_id;
                this.oauthId = oauthId;
                this.user_name = user_name;
                this.profile_image = profile_image;
                this.provider = provider;
                this.created_at = created_at;
    }
}
