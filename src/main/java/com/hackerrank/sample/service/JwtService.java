package com.hackerrank.sample.service;

import com.hackerrank.sample.config.JwtProperties;
import com.hackerrank.sample.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtService(final JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
        this.expirationTime = jwtProperties.expiration();
    }

    /**
     * Generates a signed JWT token for a specific user.
     *
     * @param username The username to be set as the token subject.
     * @return A compact URL-safe JWT string.
     */
    public String generateToken(final String username) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationTime)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token The JWT token to be parsed.
     * @return The extracted username.
     * @throws InvalidTokenException if the token is expired, tampered with, or malformed.
     */
    public String extractUsername(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Could not extract username from token", e);
        }
    }
}