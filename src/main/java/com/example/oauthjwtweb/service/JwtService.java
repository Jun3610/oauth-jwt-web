package com.example.oauthjwtweb.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;


@Getter
@Service
public class JwtService {

    private Key key;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.accessTokenExpirationMs}")
    private Long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private Long refreshTokenExpirationMs;

    @PostConstruct // Value is injected, @PostConstruct Method Started
    public void init() { // SecretKey -> BASE64 -> byte[] -> Key
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String user_id, List<String> roles) { // Set AccessToken
        JwtBuilder builder = Jwts.builder(); // JWT Method Setting
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs); // Payload:Set expiryDate
        builder.setSubject(user_id) // Set information to Token
                .claim("roles", roles) //Payload: Set roles to User
                .setIssuedAt(now) //Payload: Set Time,When Token issued
                .setExpiration(expiryDate) //Payload: Set expire time
                .signWith(key, SignatureAlgorithm.HS512); // Set Header, Signature
        return builder.compact(); // toString
    }

    public String generateRefreshToken(String user_id) { //Set RefreshToken, The same way as generateAccessToken
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
        builder.setSubject(user_id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512);
        return builder.compact();
    }

    public Claims parseClaims(String token) { // Verifying Token
        return Jwts.parserBuilder() // Set Verifying Token
                .setSigningKey(key)// Register ServerKey for Verifying
                .build().parseClaimsJws(token) // Verifying Signature with Key
                .getBody(); // Return Payload
    }

    private Date extractExpiration(String token) { //Return Expiration Time, The same way as parseClaims
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody()
                .getExpiration(); //Return Expiration Time
    }

    public boolean isTokenValid(String token) {
         Boolean ex =  extractExpiration(token).before(new Date()); // if Expiration Time is Before Current Time or Not
        return !ex; // exTrue: Expiration Time is before Current Time
    }
}


