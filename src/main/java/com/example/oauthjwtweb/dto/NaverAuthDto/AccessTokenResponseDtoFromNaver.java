package com.example.oauthjwtweb.dto.NaverAuthDto;

import lombok.Data;

@Data
public class AccessTokenResponseDtoFromNaver {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Integer expiresIn;
    
    private String error;

    private String errorDescription;
}
