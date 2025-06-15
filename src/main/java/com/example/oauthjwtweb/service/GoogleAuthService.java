package com.example.oauthjwtweb.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class GoogleAuthService {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    //Setting Session
    public void setStateInSession(HttpSession session) {
        session.setAttribute("state", UUID.randomUUID().toString());
    }

    // AuthUrlSetting
    public String setGoogleAuthUrl(HttpSession session) {
        return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly%20https%3A//www.googleapis.com/auth/calendar.readonly"
                +"&access_type=offline"
                +"&include_granted_scopes=true"
                +"&response_type=code"
                +"&state=" + session.getAttribute( "state" )
                +"&redirect_uri=" + redirectUri
                +"&client_id=" + clientId;
    }

    // AuthorizationCode -> AccessToken
    public void googleAuthorize(String code, String state, HttpSession session) {
        System.out.println("로그 확인용!!!!");
        String serverSession = (String) session.getAttribute("state");
        if (state == null || serverSession == null || state.isEmpty() || !state.equals(serverSession) || code == null || code.isEmpty()) {
            throw new RuntimeException("보안문제 발생");
        }

    }
}
