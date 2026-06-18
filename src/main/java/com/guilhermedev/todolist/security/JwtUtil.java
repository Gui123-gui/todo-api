package com.guilhermedev.todolist.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Em produção, essa chave deveria vir do application.properties, não fixa no código
    private final SecretKey key = Keys.hmacShaKeyFor(
            "minha-chave-secreta-com-pelo-menos-32-caracteres".getBytes()
    );

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}