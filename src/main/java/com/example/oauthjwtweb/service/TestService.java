package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.testDto.TestEnviornmnetVariableDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${naver.client-id}")
    private String naverClientId;

    @Value("${naver.client_secret}")
    private String naverClientSecret;

    @Value("${naver.callback.url}")
    private String naverCallbackUrl;

    @Value("${jwt.accessTokenExpirationMs}")
    private String jwtAccessTime;

    @Value("${jwt.refreshTokenExpirationMs}")
    private String jwtRefreshTime;

    public TestEnviornmnetVariableDto testEnviornmnetvarialbe() {
        TestEnviornmnetVariableDto testEnviornmnetVariableDto = new TestEnviornmnetVariableDto(
                googleClientId,
                googleClientSecret,
                googleRedirectUri,
                kakaoClientId,
                kakaoRedirectUrl,
                naverClientId,
                naverClientSecret,
                naverCallbackUrl,
                jwtAccessTime,
                jwtRefreshTime
        );
        return testEnviornmnetVariableDto;
    }
}
