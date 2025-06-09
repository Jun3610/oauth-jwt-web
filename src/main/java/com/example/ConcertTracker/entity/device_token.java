package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "device_token")
public class device_token {

    public device_token() {}

    @Id
    private String device_token;

    @Column(nullable = false)
    private String token;

    public device_token(String device_id, String token) {
        this.device_token = device_id;
        this.token = token;
    }
}
