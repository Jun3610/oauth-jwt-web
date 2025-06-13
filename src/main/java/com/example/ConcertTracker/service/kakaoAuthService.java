package com.example.ConcertTracker.service;

import com.example.ConcertTracker.dto.AccessTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.AccessTokenResponseDtoFromKakako;
import com.example.ConcertTracker.dto.kakaoAuthDto.UserInfoRequestDto;
import com.example.ConcertTracker.entity.User;
import com.example.ConcertTracker.repository.kakaoAuthRepository;
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
public class kakaoAuthService {
    final private RestTemplate restTemplate;
    final private kakaoAuthRepository kakaoAuthRepository;
    final private JwtService jwtService;

    //config made by Spring
    kakaoAuthService(
            RestTemplate restTemplate,
            kakaoAuthRepository kakaoAuthRepository,
            JwtService jwtService
                     ) {
        this.restTemplate = restTemplate;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.jwtService = jwtService;
    }

    private AccessTokenResponseDto tokenDto;
    private LocalDateTime now = LocalDateTime.now();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${REDIRECT_URL}")
    private String redirectUrl;

    // AuthorizationCode -> AccessToken
    @Transactional
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
    @Transactional
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
    public Optional<User> findOrCreateUserFromOAuth(UserInfoRequestDto userInfo) {
        Optional<User> optionalUser = kakaoAuthRepository.findByOauthId(userInfo.getId().toString())
                .or( () -> { User newUser = new User(
                        UUID.randomUUID().toString(),
                        userInfo.getId().toString(),
                        userInfo.getKakaoAccount().getProfile().getNickname(),
                        userInfo.getKakaoAccount().getProfile().getProfile_image_url(),
                        "kakao",
                        now
                );
                    kakaoAuthRepository.save(newUser);
                    return Optional.of(newUser);
                });
        return optionalUser;
    }

    // Returning JWT AccessToken, RefreshToken to Client
    @Transactional
    public AccessTokenResponseDto authWithToken(Optional<User> user) {
        String AccessToken = jwtService.generateAccessToken(user.get().getUser_id());
        String RefreshToken = jwtService.generateRefreshToken(user.get().getUser_id());
        Long accessTokenExpirationMs = jwtService.getAccessTokenExpirationMs();
        AccessTokenResponseDto tokenResponseDto = new AccessTokenResponseDto(
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