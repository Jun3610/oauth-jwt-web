package com.example.oauthjwtweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

@Configuration //Setting File
public class SecurityConfig {
    private final CorsConfig corsConfig;

    @Autowired
    public SecurityConfig(CorsConfig corsConfig) {this.corsConfig = corsConfig;}

    @Bean // Be registered as Spring Bean: Spring create and manage this Object(this method)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsFilter()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());


        return http.build();
    }
}