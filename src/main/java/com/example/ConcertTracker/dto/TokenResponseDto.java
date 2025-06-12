package com.example.ConcertTracker.dto;

import lombok.Data;

@Data
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String userId;
    private String username;

    public TokenResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        String userId,
        String username) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.username = username;
    }

}
