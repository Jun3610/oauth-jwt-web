package com.example.oauthjwtweb.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Getter
@Service
public class JwtService {

    private Key key;

    @Value("${jwt.accessTokenExpirationMs}")
    private Long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private Long refreshTokenExpirationMs;

    @PostConstruct
    public void init() { // BASE64 -> byte[] -> Key
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateAccessToken(String user_id) {
        JwtBuilder builder = Jwts.builder(); // JWT Method Setting
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);
        builder.setSubject(user_id) // Set information to AccessToken
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512) // if 'Header' or 'payLoad' was changed -> Sign x
                .setExpiration(expiryDate); //expire time
        return builder.compact();
    }

    public String generateRefreshToken(String user_id) {
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
        builder.setSubject(user_id)
                .setIssuedAt(now)
                .setExpiration(expiryDate) //expire time, difference from AccessToken
                .signWith(key, SignatureAlgorithm.HS512);
        return builder.compact();
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

