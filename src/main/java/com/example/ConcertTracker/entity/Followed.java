package com.example.ConcertTracker.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table (name = "followed")
public class Followed {

    public Followed() {}

    @Id
    private boolean followed;

    @Column (nullable = false)
    private UUID user_id;

    @Column (nullable = false)
    private UUID artist_id;

    @Column (nullable = false)
    private UUID concert_id;

    public Followed(boolean followed, UUID user_id, UUID artist_id, UUID concert_id) {
        this.followed = followed;
        this.user_id = user_id;
        this.artist_id = artist_id;
        this.concert_id = concert_id;
    }
}
