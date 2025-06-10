package com.example.ConcertTracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccessRefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UUID userId;
    private String username;
}
