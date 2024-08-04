package org.yezproject.pet.authentication.infrastructure.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public record JwtGenerator(
        String secret,
        Long expiration
) {

    public String generateToken(Map<String, Object> extractClaim, String subject) {
        Instant now = Instant.now();
        Instant hoursAfter = now.plus(expiration, ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(extractClaim)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(hoursAfter))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
