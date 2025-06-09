package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.dto.AccessTokenResponseDto;
import com.example.ConcertTracker.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public class AuthController {
    private AuthService authService;

    @PostMapping("/login/kakao")
    public String authorize(@RequestHeader String authorization_code) { // Get Authorization
        AccessTokenResponseDto AccessToken = authService.Authorize(authorization_code); // send Authorization to service
        authService.getUserInfo(AccessToken);


        return "개발";
    }
}
