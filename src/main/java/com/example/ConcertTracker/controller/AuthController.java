package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.dto.TokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.AccessTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.UserInfoRequestDto;
import com.example.ConcertTracker.entity.User;
import com.example.ConcertTracker.service.kakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api")
public class AuthController {
    private kakaoAuthService kakaoAuthService;


    // Auth -> Token, Not exist -> register -> Token
    @PostMapping("/login/kakao")
    public TokenResponseDto authorize(@RequestHeader String authorization_code) { // Get Authorization
        AccessTokenResponseDto AccessToken = kakaoAuthService.kakaoAuthorize(authorization_code); // send Authorization to service
        ResponseEntity<UserInfoRequestDto> userInfo = kakaoAuthService.kakaoGetUserInfo(AccessToken);
        Optional<User> user = kakaoAuthService.findOrCreateUserFromOAuth(userInfo);
        return kakaoAuthService.authWithToken(user);
    }
}
