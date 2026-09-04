package com.example.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private String secretKey = "";

    public JwtService() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

        SecretKey secretKeyGenerated = keyGen.generateKey();

        secretKey = Base64.getEncoder()
                .encodeToString(secretKeyGenerated.getEncoded());
    }

    // Generate JWT Token
    public String generateToken(String userName) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(System.currentTimeMillis() + 30 * 60 * 1000)
                )
                .and()
                .signWith(getKey())
                .compact();
    }

    // Convert Base64 secret key to SecretKey
    private Key getKey() {

        byte[] bytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(bytes);
    }

    // Extract username from JWT
    public String extractUserName(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate JWT
    public boolean validateToken(
            String token,
            UserDetails userDetails
    ) {

        final String username = extractUserName(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from JWT
    private Date extractExpiration(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}