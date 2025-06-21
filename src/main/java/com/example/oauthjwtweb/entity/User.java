package com.example.oauthjwtweb.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table (name = "user")
public class User {

    public User() {}

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "OAuth_id")
    private String oauthId;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @Column(name = "profile_image")
    private String profile_image;

    @Column(name = "provider")
    private String provider;

    @Column(name = "created_at")
    private LocalDateTime created_at;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(String user_id,
                String oauthId,
                String user_name,
                String profile_image,
                String provider,
                LocalDateTime created_at) {

                this.userId = user_id;
                this.oauthId = oauthId;
                this.userName = user_name;
                this.profile_image = profile_image;
                this.provider = provider;
                this.created_at = created_at;
    }
}
