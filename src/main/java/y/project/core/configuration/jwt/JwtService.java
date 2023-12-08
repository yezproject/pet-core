package y.project.core.configuration.jwt;

import y.project.core.domain.UserInfo;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserInfo userDetails);

    boolean isTokenValid(String token, UserInfo userDetails);
}
