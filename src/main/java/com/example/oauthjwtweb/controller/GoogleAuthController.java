package com.example.oauthjwtweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/google")
public class GoogleAuthController {

    @GetMapping("/get")
    public void getGoogleUrl() {
        
    }
}
