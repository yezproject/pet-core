package org.yproject.pet.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public String generateToken(String email, String tokenId) {
        final var claims = new HashMap<String, Object>();
        claims.put(Claims.ID, tokenId);
        return generateToken(claims, email);
    }

    @Override
    public boolean isTokenValid(String token, String email, Set<String> tokenIDs) {
        return isEmailValid(token, email) && !isTokenExpired(token) && isTokenIdValid(token, tokenIDs);
    }

    private String generateToken(Map<String, Object> extractClaim, String email) {
        Instant now = Instant.now();
        Instant hoursAfter = now.plus(jwtProperties.getExpiration(), ChronoUnit.HOURS);

        return Jwts.builder()
                .addClaims(extractClaim)
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(hoursAfter))
                .signWith(getSigningKey(), SignatureAlgorithm.forName(jwtProperties.getAlgorithm())).compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isEmailValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email);
    }

    private boolean isTokenIdValid(String token, Set<String> tokenIds) {
        final String extractedJwtId = extractClaim(token, Claims::getId);
        if (extractedJwtId == null) return true;
        else return tokenIds.contains(extractedJwtId);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
