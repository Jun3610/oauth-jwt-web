package com.example.oauthjwtweb.dto;

import lombok.Data;

// AccessTokenResponseDtoFromJWT.java 파일은 그대로 사용
@Data
public class AccessTokenResponseDtoFromJWT {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String userId;
    private String username;

    public AccessTokenResponseDtoFromJWT(
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
