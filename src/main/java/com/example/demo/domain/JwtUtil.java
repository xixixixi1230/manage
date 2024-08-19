package com.example.demo.domain;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "Admin-Token";  // 请使用更安全的密钥

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 1 小时过期
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
