package com.example.ConcertTracker.service;

import com.example.ConcertTracker.dto.AccessTokenResponseDto;
import com.example.ConcertTracker.repository.AuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private RestTemplate restTemplate;
    private AuthRepository authRepository;

    //config
    AuthService(RestTemplate restTemplate) { //spring made it
        this.restTemplate = restTemplate;
    }

    // Authorization -> AccessToken
    public AccessTokenResponseDto Authorize(String code) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("grant_type", "authorization_code");
        multiValueMap.add("client_id", "8df02b97528b1a8ac446e88b44cbd853");
        multiValueMap.add("redirect_uri", "http://localhost:8080/api/login/kakao");
        multiValueMap.add("code", code);

        AccessTokenResponseDto AccessToken = restTemplate.postForObject( //RestTemplate's default method: get, and
                "https://kauth.kakao.com/oauth/token", // URL
                multiValueMap, // FormData to send
                AccessTokenResponseDto.class // response type
        );
        return AccessToken;
    }

    // FindById for AccessToken
    public void getUserInfo (AccessTokenResponseDto AccessToken) {
        authRepository.
    }

}