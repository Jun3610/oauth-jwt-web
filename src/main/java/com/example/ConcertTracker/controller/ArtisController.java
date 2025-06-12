package com.example.ConcertTracker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/myArtist")
public class ArtisController {

    @PostMapping("/addArtist")
    public void addArtist() {

    }
}
