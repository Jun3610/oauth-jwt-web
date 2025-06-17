package com.example.oauthjwtweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Setting File
public class SecurityConfig {

    @Bean // Be registered as Spring Bean: Spring create and manage this Object(this method)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf( (CsrfConfigurer<HttpSecurity> csrfConsumer) -> { csrfConsumer.disable(); })
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}