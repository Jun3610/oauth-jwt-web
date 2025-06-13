package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.dto.AccessTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.UserInfoRequestDto;
import com.example.ConcertTracker.entity.User;
import com.example.ConcertTracker.service.kakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private kakaoAuthService kakaoAuthService;

    // Code -> Token
    @PostMapping("/login/kakao")
    public AccessTokenResponseDto authorize(@RequestParam String code) { // Get Authorization
        com.example.ConcertTracker.dto.kakaoAuthDto.AccessTokenResponseDto AccessToken = kakaoAuthService.kakaoAuthorize(code); // send Authorization to service
        UserInfoRequestDto userInfo = kakaoAuthService.kakaoGetUserInfo(AccessToken);
        Optional<User> user = kakaoAuthService.findOrCreateUserFromOAuth(userInfo);
        return kakaoAuthService.authWithToken(user);
    }
}
