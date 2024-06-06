package org.yezproject.pet.jwt;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public record JwtServiceImpl(
        JwtProperties jwtProperties
) implements JwtService {

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    @Override
    public boolean isTokenValid(String token, String email) {
        return isEmailValid(token, email);
    }

    private String generateToken(Map<String, Object> extractClaim, String email) {
        Instant now = Instant.now();
        Instant hoursAfter = now.plus(jwtProperties.getExpiration(), ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(extractClaim)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(hoursAfter))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isEmailValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ClaimJwtException e) {
            throw new TokenExpiredException();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
