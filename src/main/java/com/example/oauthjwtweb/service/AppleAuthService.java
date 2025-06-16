package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.repository.NaverAuthRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class AppleAuthService {

    final private SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler;
    final private NaverAuthRepository naverAuthRepository;
    final private RestTemplate restTemplate;
    final private JwtService jwtService;

    public AppleAuthService(SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler,
                            NaverAuthRepository naverAuthRepository,
                            RestTemplate restTemplate,
                            JwtService jwtService
    ) {
        this.webSecurityExpressionHandler = webSecurityExpressionHandler;
        this.naverAuthRepository = naverAuthRepository;
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
    }

    public String setAppleAuthUrl(HttpSession session) {
        session.setAttribute("state", UUID.randomUUID().toString());
        return "https://appleid.apple.com/auth/authorize"
                "&client_id" + 

    }

}
