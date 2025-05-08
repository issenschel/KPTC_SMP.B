package com.example.kptc_smp.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Duration accessExpirationMs;

    @Value("${jwt.refresh.expiration}")
    private Duration refreshExpirationMs;

    public String generateAccessToken(String username,UUID tokenUUID) {
        Date issuedDate = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedDate)
                .claim("uuid", tokenUUID)
                .setExpiration(new Date(issuedDate.getTime() + accessExpirationMs.toMillis()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date issuedDate = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(issuedDate.getTime() + refreshExpirationMs.toMillis()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Instant getRefreshTokenExpiration() {
        return Instant.now().plusMillis(refreshExpirationMs.toMillis());
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public Optional<UUID> getTokenUUID(String token) {
        String tokenUUIDString = getAllClaimsFromToken(token).get("uuid", String.class);
        if (tokenUUIDString != null) {
            return Optional.of(UUID.fromString(tokenUUIDString));
        } else {
            return Optional.empty();
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


}
