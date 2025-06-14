package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.KakaoAuthDto.AccessTokenResponseDtoFromKakako;
import com.example.oauthjwtweb.dto.KakaoAuthDto.UserInfoRequestDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.service.KakaoAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/kakao")
public class KakaoAuthController {

    final private KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    //SetUrl
    @GetMapping("/get")
    public String getKakaoUrl() {
        return kakaoAuthService.setKakaoUrl();
    }

    // Code -> Token
    @PostMapping("/auth")
    public AccessTokenResponseDtoFromJWT authorizeKakao(@RequestParam String code) { // Get Authorization
        AccessTokenResponseDtoFromKakako AccessToken = kakaoAuthService.kakaoAuthorize(code); // send Authorization to service
        UserInfoRequestDto userInfo = kakaoAuthService.kakaoGetUserInfo(AccessToken);
        Optional<User> user = kakaoAuthService.findOrCreateUserFromOAuth_kakao(userInfo);
        return kakaoAuthService.authWithToken_kakao(user);
    }
}
