package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.googleAuthDto.AccessTokenResponeDtoFromGoogle;
import com.example.oauthjwtweb.dto.googleAuthDto.UserInfoRequestDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.GoogleAuthRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GoogleAuthService {

    private final GoogleAuthRepository googleAuthRepository;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private Logger logger = Logger.getLogger(GoogleAuthService.class.getName());

    @Autowired
    public GoogleAuthService(GoogleAuthRepository googleAuthRepository, JwtService jwtService, RestTemplate restTemplate) {
        this.googleAuthRepository = googleAuthRepository;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    private AccessTokenResponseDtoFromJWT tokenDto;
    private LocalDateTime now = LocalDateTime.now();

    //Setting Session
    public void setStateInSession(HttpSession session) {
        session.setAttribute("state", UUID.randomUUID().toString());
    }

    // AuthUrlSetting
    public String setGoogleAuthUrl(HttpSession session) {
        return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=" + URLEncoder.encode(
                "openid https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
                StandardCharsets.UTF_8
        ) +
                "&access_type=offline" +
                "&include_granted_scopes=true" +
                "&response_type=code" +
                "&state=" + session.getAttribute("state") +
                "&redirect_uri=" + redirectUri +
                "&client_id=" + clientId;
    }

    // AuthorizationCode -> AccessToken
    public AccessTokenResponeDtoFromGoogle googleAuthorize(String code, String state, HttpSession session) {
        String serverSession = (String) session.getAttribute("state");
        if (state == null || serverSession == null || state.isEmpty() || !state.equals(serverSession) || code == null || code.isEmpty()) {
            throw new RuntimeException("보안문제 발생");
        }
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            HttpHeaders headers = new HttpHeaders();

            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("code", code);
            params.add("grant_type", "authorization_code");
            params.add("redirect_uri", redirectUri);
            headers.set("Content-Type", "application/x-www-form-urlencoded");


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<AccessTokenResponeDtoFromGoogle> accessToken = restTemplate.exchange(
                    "https://oauth2.googleapis.com/token",
                    HttpMethod.POST,
                    request,
                    AccessTokenResponeDtoFromGoogle.class
            );
            return accessToken.getBody();
        }
        catch (HttpStatusCodeException e) {
            logger.severe("HttpStatusCodeException: " + e.getMessage());
            throw new RuntimeException("Error from Google Server: " + e.getMessage(), e);
        }
        catch (ResourceAccessException e) {
            logger.severe("ResourceAccessException: " + e.getMessage());
            throw new RuntimeException("Network error: " + e.getMessage(), e);
        }
        catch (RestClientException e) {
            logger.severe("RestClientException: " + e.getMessage());
            throw new RuntimeException("RestClientException occurred: " + e.getMessage(), e);
        }
        catch (Exception e) {
            logger.severe("General Exception: " + e.getMessage());
            throw new RuntimeException("Error occurred: " + e.getMessage(), e);
        }
    }

    // Get UserInfo with AccessToken from Google
    public UserInfoRequestDto  googleGetUserInfo (AccessTokenResponeDtoFromGoogle accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken.getAccess_token());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoRequestDto> userInfo = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                httpEntity,
                UserInfoRequestDto.class
        );
        return userInfo.getBody();
    }

    // FindByAuthId in DataBase Or Set UserInfo to DataBase-> UserInfo
    @Transactional
    public Optional<User> findOrCreateUserFromOAuth_google(UserInfoRequestDto userInfo) {
        Optional<User> OptionalUser = googleAuthRepository.findByOauthId(userInfo.getSub())
                .or(() -> {
                    User newUser = new User(
                            UUID.randomUUID().toString(),
                            userInfo.getSub(),
                            userInfo.getName(),
                            userInfo.getPicture(),
                            "GOOGLE",
                            now
                    );
                            googleAuthRepository.save(newUser);
                            return Optional.of(newUser);
                        }
                );
            return OptionalUser;
    }

    // Returning JWT AccessToken, RefreshToken to Client By UserId
    public AccessTokenResponseDtoFromJWT authWithToken_google(Optional<User> user) {
        List<String> roles = user.get().getRoles()
                .stream().map(role -> role.getRoleName())  // Role 객체에서 권한명 추출
                .collect(Collectors.toList());
        AccessTokenResponseDtoFromJWT accessTokenResponseDtoFromJWT
                = new AccessTokenResponseDtoFromJWT(
                jwtService.generateAccessToken(user.get().getUserId(), roles),
                jwtService.generateRefreshToken(user.get().getUserId()),
                "Bearer",
                jwtService.getAccessTokenExpirationMs(),
                user.get().getUserId(),
                user.get().getUserName()
        );
        return accessTokenResponseDtoFromJWT;
    }
}
