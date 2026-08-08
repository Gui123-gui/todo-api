package com.guilhermedev.todolist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // NOVO: abre o token e devolve todos os dados (claims) de dentro dele
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // NOVO: atalho para pegar só o email (subject) do token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // NOVO: atalho para pegar só o userId do token
    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    // NOVO: verifica se o token é válido (assinatura correta e não expirado)
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}