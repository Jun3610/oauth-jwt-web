package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.dto.AccessRefreshTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.AccessTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.UserInfoResponseDto;
import com.example.ConcertTracker.service.kakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public class AuthController {
    private kakaoAuthService kakaoAuthService;

    @PostMapping("/login/kakao")
    public AccessRefreshTokenResponseDto authorize(@RequestHeader String authorization_code) { // Get Authorization
        AccessTokenResponseDto AccessToken = kakaoAuthService.kakaoAuthorize(authorization_code); // send Authorization to service
        ResponseEntity<UserInfoResponseDto> userInfo = kakaoAuthService.kakaoGetUserInfo(AccessToken);
        AccessRefreshTokenResponseDto accessRefreshTokenResponseDto = kakaoAuthService.authWithToken(userInfo);
        return accessRefreshTokenResponseDto;
    }
}
