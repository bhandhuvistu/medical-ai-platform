package com.example.medicalai.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.*;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirySeconds;

    public JwtService(@Value("${security.jwt.secret:dev-secret-key-change}") String secret,
                      @Value("${security.jwt.expiry-seconds:72000}") long expirySeconds){
        this.key = Keys.hmacShaKeyFor(Arrays.copyOf(secret.getBytes(), 32));
        this.expirySeconds = expirySeconds;
    }

    public String issueToken(String username, Collection<String> roles){
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", String.join(",", roles))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirySeconds)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
