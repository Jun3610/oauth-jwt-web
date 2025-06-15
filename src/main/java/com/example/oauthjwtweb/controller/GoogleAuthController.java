package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.service.GoogleAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    final private GoogleAuthService googleAuthService;

    GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/get")
    public String getGoogleUrl(HttpSession session) {
        googleAuthService.setStateInSession(session);
        return googleAuthService.setGoogleAuthUrl(session);
    }

    @PostMapping("/auth")
    public void authorizeGoogle (@RequestParam String code,
                                 @RequestParam String state,
                                 HttpSession session
    ) {
        System.out.println("로그확인용");
        googleAuthService.googleAuthorize(code, state, session);
    }
}
