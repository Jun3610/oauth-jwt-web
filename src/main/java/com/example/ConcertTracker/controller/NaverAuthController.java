package com.example.ConcertTracker.controller;

import com.example.ConcertTracker.service.NaverAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class NaverAuthController {

    final protected NaverAuthService naverAuthService;

    public NaverAuthController(NaverAuthService naverAuthService) {
        this.naverAuthService = naverAuthService;
    }

    @PostMapping("/naver")
    public void authorizeNaver(@RequestParam String code) {
        naverAuthService.authorizeNaver(code);

    }

}
