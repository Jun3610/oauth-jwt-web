package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.kakaoAuthDto.AccessTokenResponseDtoFromKakako;
import com.example.oauthjwtweb.dto.kakaoAuthDto.UserInfoRequestDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.KakaoAuthRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class KakaoAuthService {
    final private RestTemplate restTemplate;
    final private KakaoAuthRepository kakaoAuthRepository;
    final private JwtService jwtService;

    //config made by Spring
    KakaoAuthService(
            RestTemplate restTemplate,
            KakaoAuthRepository kakaoAuthRepository,
            JwtService jwtService
    ) {
        this.restTemplate = restTemplate;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.jwtService = jwtService;
    }

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private AccessTokenResponseDtoFromJWT tokenDto;
    private LocalDateTime now = LocalDateTime.now();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    // AuthUrlSetting
    public String setKakaoAuthUrl(HttpSession session) {
        session.setAttribute("state", UUID.randomUUID().toString());
        return "https://kauth.kakao.com/oauth/authorize?client_id="
                + kakaoClientId
                + "&redirect_uri="
                + redirectUrl
                + "&response_type=code"
                + "&state="
                + session.getAttribute("state");
    }

    // AuthorizationCode -> AccessToken
    public AccessTokenResponseDtoFromKakako kakaoAuthorize(String code, String state, HttpSession session) {

        String serverSession = (String) session.getAttribute("state");

        if (state == null || serverSession == null || state.isEmpty() || !state.equals(serverSession) || code == null || code.isEmpty()) {
            throw new RuntimeException("보안문제 발생");
        }
        try {
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
        } catch (HttpStatusCodeException e) {
            logger.error("HttpStatusCodeException: " + e.getMessage());
            throw new RuntimeException("Error from Google Server: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            logger.error("ResourceAccessException: " + e.getMessage());
            throw new RuntimeException("Network error: " + e.getMessage(), e);
        } catch (RestClientException e) {
            logger.error("RestClientException: " + e.getMessage());
            throw new RuntimeException("RestClientException occurred: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("General Exception: " + e.getMessage());
            throw new RuntimeException("Error occurred: " + e.getMessage(), e);
        }
    }

    // Get UserInfo with AccessToken from Kakao
    public UserInfoRequestDto kakaoGetUserInfo(AccessTokenResponseDtoFromKakako AccessTokenFromKakao) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + AccessTokenFromKakao.getAccess_token());

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
                .or(() -> {
                    User newUser = new User(
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