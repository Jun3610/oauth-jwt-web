package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "venue")
public class venue {

    public venue() {}

    @Id
    private UUID venue_id;

    @Column(nullable = false)
    private UUID concert_id;

    private String location;

    private boolean parking;

    public venue(UUID venue_id, UUID concert_id, String location, boolean parking) {
        this.venue_id = UUID.randomUUID();
        this.concert_id = concert_id;
        this.location = location;
        this.parking = parking;
    }
}
