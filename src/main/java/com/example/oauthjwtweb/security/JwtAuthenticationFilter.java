package com.example.oauthjwtweb.security;

import com.example.oauthjwtweb.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Helper Method, Not overriding
    private String extractToken(HttpServletRequest request) { //Extracting Token Value
        String header = request.getHeader("Authorization"); //Return Value for Key Authorization
        if (header != null && header.startsWith("Bearer ")) { //
            return header.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter 호출 - 요청 URL: " + request.getRequestURI());
        String token = extractToken(request);

        if (token != null && jwtService.isTokenValid(token)) {  // verifying Token
            Claims claims = jwtService.parseClaims(token); // Extract UserID
            String userId = claims.getSubject(); // Extract sub, Owner
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId); // Change the Type to UserDetails by Service
            Authentication auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()); //
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}