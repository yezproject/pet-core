package org.yezproject.pet.gateway;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Objects;

public record JwtHelper(
        String secret,
        Long expiration
) {
    public JwtHelper {
        Objects.requireNonNull(secret);
        Objects.requireNonNull(expiration);
    }

    public JwtDetails extractPayload(String token) throws TokenExpiredException, TokenInvalidException {
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
        var uid = claims.get("uid", String.class);
        return new JwtDetails(uid, email, expiration, name);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
