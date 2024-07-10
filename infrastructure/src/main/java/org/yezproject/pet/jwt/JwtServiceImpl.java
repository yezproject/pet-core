package org.yezproject.pet.jwt;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public record JwtServiceImpl(
        JwtProperties jwtProperties
) implements JwtService {

    @Override
    public String generateToken(JwtUserRequest jwtUserRequest) {
        final var claims = new HashMap<String, Object>();
        claims.put("name", jwtUserRequest.name());
        return generateToken(claims, jwtUserRequest.email());
    }

    @Override
    public JwtPayload extractPayload(String token) throws TokenExpiredException, TokenInvalidException {
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (ClaimJwtException e) {
            throw new TokenExpiredException("claim jwt exception");
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException("malformed jwt exception");
        } catch (SignatureException e) {
            throw new TokenInvalidException("signature exception");
        }
        var email = claims.getSubject();
        var expiration = claims.getExpiration().toInstant();
        var name = claims.get("name", String.class);
        return new JwtPayload(email, expiration, name);
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

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
