package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.dto.AccessTokenResponseDto;
import com.example.ConcertTracker.dto.kakaoAuthDto.AccessTokenResponseDtoFromKakako;
import com.example.ConcertTracker.dto.kakaoAuthDto.UserInfoRequestDto;
import com.example.ConcertTracker.entity.User;
import com.example.ConcertTracker.service.kakaoAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class KakaoAuthController {

    final private kakaoAuthService kakaoAuthService;

    public KakaoAuthController(kakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    // Code -> Token
    @PostMapping("/login/kakao")
    public AccessTokenResponseDto authorize(@RequestParam String code) { // Get Authorization
        AccessTokenResponseDtoFromKakako AccessToken = kakaoAuthService.kakaoAuthorize(code); // send Authorization to service
        UserInfoRequestDto userInfo = kakaoAuthService.kakaoGetUserInfo(AccessToken);
        Optional<User> user = kakaoAuthService.findOrCreateUserFromOAuth(userInfo);
        return kakaoAuthService.authWithToken(user);
    }
}
