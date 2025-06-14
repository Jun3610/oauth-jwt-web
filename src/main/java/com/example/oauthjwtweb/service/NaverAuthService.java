package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.dto.NaverAuthDto.AccessTokenResponseDtoFromNaver;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class NaverAuthService {

    private final HttpSession httpSession;
    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client_secret}")
    private String clientSecret;

    @Value("${naver.callback.url}")
    private String callbackUrl;

    RestTemplate restTemplate = new RestTemplate();

    public NaverAuthService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    // session Setting in Url -> to Client
    @Transactional
    public String createNaverLoginURL(HttpSession session) {
        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);
        return "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + callbackUrl
                + "&state=" + state;
    }

    // Code, State -> AccessToken
    @Transactional
    public AccessTokenResponseDtoFromNaver naverAuthorize (

            String code,
            String state,
            String error,
            String errorDescription) {

        HttpSession session = httpSession;
        String serverSession = (String) session.getAttribute("state");


        if (state == null || serverSession == null || state.isEmpty() || !state.equals(serverSession) || code == null || code.isEmpty()) {
            throw new RuntimeException("보안 문제가 발생하였습니다.");
        }

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap);


        try {
            multiValueMap.add("grant_type", "authorization_code");
            multiValueMap.add("client_id", clientId);
            multiValueMap.add("client_secret", clientSecret);
            multiValueMap.add("code", code);
            multiValueMap.add("state", state);

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
}
