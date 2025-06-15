package com.example.oauthjwtweb.dto.testDto;

import lombok.Data;

@Data
public class TestEnviornmnetVariableDto {

    private String googleClientId;

    private String googleClientSecret;

    private String googleRedirectUri;

    private String kakaoClientId;

    private String kakaoRedirectUrl;

    private String naverClientId;

    private String naverClientSecret;

    private String naverCallbackUrl;

    private String jwtAccessTime;

    private String jwtRefreshTime;

    public TestEnviornmnetVariableDto(
            String googleClientId,
            String googleClientSecret,
            String googleRedirectUri,
            String kakaoClientId,
            String kakaoRedirectUrl,
            String naverClientId,
            String naverClientSecret,
            String naverCallbackUrl,
            String jwtAccessTime,
            String jwtRefreshTime
    ) {
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
        this.googleRedirectUri = googleRedirectUri;
        this.kakaoClientId = kakaoClientId;
        this.kakaoRedirectUrl = kakaoRedirectUrl;
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
        this.naverCallbackUrl = naverCallbackUrl;
        this.jwtAccessTime = jwtAccessTime;
        this.jwtRefreshTime = jwtRefreshTime;

    }
}
