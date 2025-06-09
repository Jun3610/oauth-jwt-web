package com.example.ConcertTracker.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table (name = "artist_concert")
public class Artist_Concert {

    public Artist_Concert() {}

    @Id
    private UUID concert_id;

    @Column (nullable = false)
    private UUID artist_id;

    @Column (nullable = false)
    private String created_by_user_id;

    private  String title;

    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String price;

    private String reservation_time;

    public Artist_Concert(UUID concert_id, UUID artist_id, String created_by_user_id, String title, LocalDate date, String description, String price) {
        this.concert_id = UUID.randomUUID();
        this.artist_id = artist_id;
        this.created_by_user_id = created_by_user_id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.price = price;
    }
}
