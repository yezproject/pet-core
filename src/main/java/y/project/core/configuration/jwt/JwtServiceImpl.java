package y.project.core.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import y.project.core.domain.UserInfo;

import java.security.Key;
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
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserInfo userInfo) {
        return generateToken(new HashMap<>(), userInfo);
    }

    @Override
    public boolean isTokenValid(String token, UserInfo userInfo) {
        final String username = extractUserName(token);
        return username.equals(userInfo.getUsername()) && !isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> extractClaim, UserInfo userInfo) {
        Instant now = Instant.now();
        Instant hoursAfter = now.plus(jwtProperties.getExpiration(), ChronoUnit.HOURS);

        return Jwts.builder()
                .addClaims(extractClaim)
                .setSubject(userInfo.getUsername())
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
