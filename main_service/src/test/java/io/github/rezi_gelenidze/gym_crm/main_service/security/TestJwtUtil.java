package io.github.rezi_gelenidze.gym_crm.main_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Profile("integration-test")
public class TestJwtUtil {

    private final Key key;

    public TestJwtUtil(@Value("${app.auth.jwt-secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public HttpHeaders createAuthHeaders(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateToken(username));
        return headers;
    }
}