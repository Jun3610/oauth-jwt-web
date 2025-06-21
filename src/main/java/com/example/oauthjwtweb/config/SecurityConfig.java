package com.example.oauthjwtweb.config;

import com.example.oauthjwtweb.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Setting File
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(CorsConfig corsConfig,
                          JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.corsConfig = corsConfig;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean // Be registered as Spring Bean: Spring create and manage this Object(this method)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // For JWT, Not Required
                .cors(cors -> cors.configurationSource(corsConfig.corsFilter()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/admin").hasRole("DEVELOPMENT")
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}