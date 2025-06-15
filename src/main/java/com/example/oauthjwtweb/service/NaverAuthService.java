package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.naverAuthDto.AccessTokenResponseDtoFromNaver;
import com.example.oauthjwtweb.dto.naverAuthDto.UserInfoFromNaverByTokenDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.NaverAuthRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class NaverAuthService {

    final private SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler;
    final private NaverAuthRepository naverAuthRepository;
    final private RestTemplate restTemplate;
    final private JwtService jwtService;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client_secret}")
    private String clientSecret;

    @Value("${naver.callback.url}")
    private String callbackUrl;

    public NaverAuthService(SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler,
                            NaverAuthRepository naverAuthRepository,
                            RestTemplate restTemplate,
                            JwtService jwtService
        ) {
        this.webSecurityExpressionHandler = webSecurityExpressionHandler;
        this.naverAuthRepository = naverAuthRepository;
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
    }

    private AccessTokenResponseDtoFromJWT tokenDto;
    private LocalDateTime now = LocalDateTime.now();

    // UUID -> Session
    public void setStateInSession (HttpSession session) {
        session.setAttribute("state", UUID.randomUUID().toString());
    }

    // session Setting in Url -> to Client
    public String SetNaverAuthUrl(HttpSession session) {
        return "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + callbackUrl
                + "&state=" + (String) session.getAttribute("state");
    }

    // Code, State -> AccessToken
    public AccessTokenResponseDtoFromNaver naverAuthorize (
        String code,
        String state,
        String error,
        String errorDescription,
        HttpSession session
        ) {
        String serverSession = (String) session.getAttribute("state");
        if (state == null || serverSession == null || state.isEmpty() || !state.equals(serverSession) || code == null || code.isEmpty()) {
            throw new RuntimeException("보안 문제가 발생하였습니다.");
        }
        try {
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("grant_type", "authorization_code");
            multiValueMap.add("client_id", clientId);
            multiValueMap.add("client_secret", clientSecret);
            multiValueMap.add("code", code);
            multiValueMap.add("state", state);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, headers);
            ResponseEntity<AccessTokenResponseDtoFromNaver> TokenEntitiy = restTemplate.exchange(
                    "https://nid.naver.com/oauth2.0/token",
                    HttpMethod.POST,
                    httpEntity,
                    AccessTokenResponseDtoFromNaver.class
            );
            return TokenEntitiy.getBody();
        }
        catch (HttpStatusCodeException e) {
            System.err.println("From Naver Error Mensaage: " + error);
            System.err.println("From Server Error Message: " +  e.getMessage());
            throw new RuntimeException(errorDescription);
        }
        catch (ResourceAccessException e) {
            System.err.println("From Naver Error Mensaage: " + error);
            System.err.println("From Server Error Message: " +  e.getMessage());
            throw new RuntimeException(errorDescription);
        }
        catch (RestClientException e) {
            System.err.println("From Naver Error Mensaage: " + error);
            System.err.println("From Server Error Message: " +  e.getMessage());
            throw new RuntimeException(errorDescription);
        }
        catch (Exception e) {
            System.err.println("From Naver Error Mensaage: " + error);
            System.err.println("From Server Error Message: " +  e.getMessage());
            throw new RuntimeException(errorDescription);
        }
    }

    //AccessToken -> UserInfo
    public UserInfoFromNaverByTokenDto authByToken(AccessTokenResponseDtoFromNaver accessTokenResponseDtoFromNaver) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessTokenResponseDtoFromNaver.getAccessToken());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserInfoFromNaverByTokenDto> userInfoFromKakakoByTokenDtoResponseEntity = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                httpEntity,
                UserInfoFromNaverByTokenDto.class
        );
        return userInfoFromKakakoByTokenDtoResponseEntity.getBody();
    }

    // FindByAuthId in DataBase Or Set UserInfo to DataBase-> UserInfo
    @Transactional
    public Optional<User> findOrCreateUserFromOAuth_naver(UserInfoFromNaverByTokenDto userInfo) {
        Optional<User> optionalUser = naverAuthRepository.findByOauthId(userInfo.getResponse().getId())
                .or( () -> {
                    User newUser = new User(
                            UUID.randomUUID().toString(),
                            userInfo.getResponse().getId(),
                            userInfo.getResponse().getName(),
                            userInfo.getResponse().getProfile_image(),
                            "NAVER",
                            now
                    );
                    naverAuthRepository.save(newUser);
                    return Optional.of(newUser);
                });
        return optionalUser;
    }

    // Returning JWT AccessToken, RefreshToken to Client By UserId
    public AccessTokenResponseDtoFromJWT authWithToken_naver(Optional<User> user) {
        AccessTokenResponseDtoFromJWT accessTokenResponseDtoFromJWT =
                new AccessTokenResponseDtoFromJWT(
                jwtService.generateAccessToken(user.get().getUser_id()),
                jwtService.generateRefreshToken(user.get().getUser_id()),
                "Bearer",
                jwtService.getAccessTokenExpirationMs(),
                user.get().getUser_id(),
                user.get().getUser_name()
        );
        return accessTokenResponseDtoFromJWT;
    }
}
