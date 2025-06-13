package com.example.ConcertTracker.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class NaverAuthService {

    @Transactional
    public void authorizeNaver(@RequestParam String code) {



    }

}
