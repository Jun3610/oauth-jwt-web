package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.KakaoAuthDto.AccessTokenResponseDtoFromKakako;
import com.example.oauthjwtweb.dto.KakaoAuthDto.UserInfoRequestDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.kakaoAuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class KakaoAuthService {
    final private RestTemplate restTemplate;
    final private kakaoAuthRepository kakaoAuthRepository;
    final private JwtService jwtService;

    //config made by Spring
    KakaoAuthService(
            RestTemplate restTemplate,
            kakaoAuthRepository kakaoAuthRepository,
            JwtService jwtService
        ) {
        this.restTemplate = restTemplate;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.jwtService = jwtService;
    }

    private AccessTokenResponseDtoFromJWT tokenDto;
    private LocalDateTime now = LocalDateTime.now();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;
    public String setKakaoUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id="
                + kakaoClientId
                + "&redirect_uri="
                + redirectUrl
                + "&response_type=code";
    }

    // AuthorizationCode -> AccessToken
    public AccessTokenResponseDtoFromKakako kakaoAuthorize(String code) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("grant_type", "authorization_code");
        multiValueMap.add("client_id", kakaoClientId);
        multiValueMap.add("redirect_uri", redirectUrl);
        multiValueMap.add("code", code);
        AccessTokenResponseDtoFromKakako AccessToken = restTemplate.postForObject( //RestTemplate's default method: get, and
                "https://kauth.kakao.com/oauth/token", // URL
                multiValueMap, // FormData to send
                AccessTokenResponseDtoFromKakako.class // response type
        );
        return AccessToken;
    }

    // Get UserInfo with AccessToken from Kakao
    public UserInfoRequestDto kakaoGetUserInfo (AccessTokenResponseDtoFromKakako AccessTokenFromKakao) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + AccessTokenFromKakao.getAccess_token());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoRequestDto> userInfo = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                httpEntity,
                UserInfoRequestDto.class
        );
        return userInfo.getBody();
    }

    // FindByAuthId in DataBase Or Set UserInfo to DataBase-> UserInfo
    @Transactional
    public Optional<User> findOrCreateUserFromOAuth_kakao(UserInfoRequestDto userInfo) {
        Optional<User> optionalUser = kakaoAuthRepository.findByOauthId(userInfo.getId().toString())
                .or( () -> { User newUser = new User(
                        UUID.randomUUID().toString(),
                        userInfo.getId().toString(),
                        userInfo.getKakaoAccount().getProfile().getNickname(),
                        userInfo.getKakaoAccount().getProfile().getProfile_image_url(),
                        "KAKAO",
                        now
                );
                    kakaoAuthRepository.save(newUser);
                    return Optional.of(newUser);
                });
        return optionalUser;
    }

    // Returning JWT AccessToken, RefreshToken to Client By UserId
    public AccessTokenResponseDtoFromJWT authWithToken_kakao(Optional<User> user) {
        String AccessToken = jwtService.generateAccessToken(user.get().getUser_id());
        String RefreshToken = jwtService.generateRefreshToken(user.get().getUser_id());
        Long accessTokenExpirationMs = jwtService.getAccessTokenExpirationMs();
        AccessTokenResponseDtoFromJWT tokenResponseDto = new AccessTokenResponseDtoFromJWT(
                AccessToken,
                RefreshToken,
                "Bearer",
                accessTokenExpirationMs,
                user.get().getUser_id(),
                user.get().getUser_name()
        );
        return tokenResponseDto;
    }
}