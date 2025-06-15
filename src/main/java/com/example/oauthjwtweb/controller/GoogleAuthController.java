package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.dto.AccessTokenResponseDtoFromJWT;
import com.example.oauthjwtweb.dto.googleAuthDto.AccessTokenResponeDtoFromGoogle;
import com.example.oauthjwtweb.dto.googleAuthDto.UserInfoRequestDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.GoogleAuthRepository;
import com.example.oauthjwtweb.service.GoogleAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    final private GoogleAuthService googleAuthService;
    private final GoogleAuthRepository googleAuthRepository;


    GoogleAuthController(GoogleAuthService googleAuthService, GoogleAuthRepository googleAuthRepository) {
        this.googleAuthService = googleAuthService;
        this.googleAuthRepository = googleAuthRepository;
    }

    @GetMapping("/get")
    public String getGoogleUrl(HttpSession session) {
        googleAuthService.setStateInSession(session);
        return googleAuthService.setGoogleAuthUrl(session);
    }

    @PostMapping("/auth")
    public AccessTokenResponseDtoFromJWT authorizeGoogle (
            @RequestParam String code,
            @RequestParam String state,
            HttpSession session
    ) {
        AccessTokenResponeDtoFromGoogle accessToken = googleAuthService.googleAuthorize(code, state, session);
        UserInfoRequestDto userInfo =  googleAuthService.googleGetUserInfo(accessToken);
        Optional<User> optionalUser = googleAuthService.findOrCreateUserFromOAuth_google(userInfo);
        return googleAuthService.authWithToken_google(optionalUser);
    }

}
