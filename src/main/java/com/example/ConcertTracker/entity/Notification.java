package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    public Notification() {}

    @Id
    private UUID notification_id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private UUID artist_id;

    public Notification(UUID notification_id, UUID user_id, UUID artist_id) {
        this.notification_id = UUID.randomUUID();
        this.user_id = user_id;
        this.artist_id = artist_id;
    }
}
