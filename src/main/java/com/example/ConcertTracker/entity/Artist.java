package com.example.ConcertTracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "artist")
public class Artist {

    public Artist() {}

    @Id
    private UUID artist_id;

    @Column(nullable = false)
    private String created_by_user_id;

    private String artist_name;

    private String artist_photo;

    public Artist(String created_by_user_id, String artist_name, String artist_photo) {
        this.artist_id = UUID.randomUUID();
        this.created_by_user_id = created_by_user_id;
        this.artist_name = artist_name;
        this.artist_photo = artist_photo;
    }
}
