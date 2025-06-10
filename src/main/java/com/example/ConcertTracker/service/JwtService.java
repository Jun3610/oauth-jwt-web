package com.example.ConcertTracker.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private Key key;

    @Value("${jwt.secret.key}")
    private String secret;

    @Value("${jwt.accessTokenExpirationMs}")
    private Long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private Long refreshTokenExpirationMs;

    @PostConstruct
    public void init() { // BASE64 -> byte[] -> Key
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UUID user_id) {
        JwtBuilder builder = Jwts.builder(); // JWT Method Setting
        Date now =  new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);
        builder.setSubject(user_id.toString()) // Set information to AccessToken
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512) // if 'Header' or 'payLoad' was changed -> Sign x
                .setExpiration(expiryDate); //expire time

        return builder.compact();
    }

    public String generateRefreshToken(UUID user_id) {
        JwtBuilder builder = Jwts.builder();
        Date now =  new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
        builder.setSubject(user_id.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate) //expire time, difference from AccessToken
                .signWith(key, SignatureAlgorithm.HS512);

        return builder.compact();
    }

    // TokenExpirationMs -> FrontEnd
    public Long getAccessTokenExpirationMs() {return accessTokenExpirationMs;}
    public Long getRefreshTokenExpirationMs() {return refreshTokenExpirationMs;}
}
