package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.service.NaverAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naver")
public class NaverAuthController {

    final protected NaverAuthService naverAuthService;

    public NaverAuthController(NaverAuthService naverAuthService) {
        this.naverAuthService = naverAuthService;
    }

    @PostMapping("/url")
    public String loginUrl(HttpSession session) {
        return naverAuthService.createNaverLoginURL(session);
    }

    @GetMapping("/auth")
    public void callBackToAccessToken(
            @RequestParam String code,
            @RequestParam String state,
            @RequestParam (required = false) String error,
            @RequestParam (required = false) String errorDescription
    ) {
    }
}
