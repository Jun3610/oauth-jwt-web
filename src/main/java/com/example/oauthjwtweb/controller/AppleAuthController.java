package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.service.AppleAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apple")
public class AppleAuthController {

    final private AppleAuthService appleAuthService;

    AppleAuthController(AppleAuthService appleAuthService) {this.appleAuthService = appleAuthService;}

    @GetMapping("/get")
    public void getAppleUrl(HttpSession session) {
        appleAuthService.setAppleAuthUrl(session);
    }

}
