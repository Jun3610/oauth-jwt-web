package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.dto.NaverAuthDto.AccessTokenResponseDtoFromNaver;
import com.example.oauthjwtweb.dto.NaverAuthDto.UserInfoFromKakakoByTokenDto;
import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.service.NaverAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/naver")
public class NaverAuthController {

    final protected NaverAuthService naverAuthService;

    public NaverAuthController(NaverAuthService naverAuthService) {
        this.naverAuthService = naverAuthService;
    }

    @PostMapping("/get")
    public String setUrlWithSession(HttpSession session) { // Set By Spring
        naverAuthService.setStateInSession(session);
        return naverAuthService.createNaverLoginURL(session);
    }

    @GetMapping("/auth")
    public void callBackToAccessToken(
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam (required = false) String error,
            @RequestParam (required = false) String errorDescription,
            HttpSession session
    ) {
        AccessTokenResponseDtoFromNaver accessTokenResponseDtoFromNaver = naverAuthService.naverAuthorize(
                code, state, error, errorDescription,session
        );
        UserInfoFromKakakoByTokenDto userInfo = naverAuthService.authByToken(accessTokenResponseDtoFromNaver);
        Optional<User> optionalUser = naverAuthService.findOrCreateUserFromOAuth_naver(userInfo);
        naverAuthService.authWithToken_naver(optionalUser);
    }
}
