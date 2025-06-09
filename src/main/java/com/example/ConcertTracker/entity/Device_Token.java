package com.example.ConcertTracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "device_token")
public class Device_Token {

    public Device_Token() {}

    @Id
    private String device_token;

    @Column(nullable = false)
    private String token;

    public Device_Token(String device_id, String token) {
        this.device_token = device_id;
        this.token = token;
    }
}
