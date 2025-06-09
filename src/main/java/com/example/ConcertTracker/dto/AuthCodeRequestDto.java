package com.example.ConcertTracker.dto;


import lombok.Data;

@Data
public class AuthCodeRequestDto {

    final private String authCode;

    public AuthCodeRequestDto(String authCode) {
        this.authCode = authCode;
    }
}
