package org.yezproject.pet.transaction.infrastructure.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yezproject.pet.transaction.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.transaction.application.jwt.JwtService;
import org.yezproject.pet.transaction.application.user.driven.AuthService;
import org.yezproject.pet.transaction.infrastructure.web.security.UserInfo;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PetAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthService authService;
    private final ApiTokenService apiTokenService;

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            final var token = bearerToken.substring(7);
            return Optional.of(token);
        }
        return Optional.empty();
    }

    private boolean isApiToken(String bearerToken) {
        return bearerToken.startsWith("yzp_");
    }

    private UserInfo authenticateByApiToken(String apiToken)
            throws ApiTokenService.InvalidTokenException, AuthService.UserNotFoundException {
        final var userId = apiTokenService.verify(apiToken).userId();
        return Optional.of(authService.loadUserById(userId))
                .map(it -> new UserInfo(it.userId(), it.email()))
                .orElseThrow();
    }

    private UserInfo authenticationByJwtToken(String jwtToken) throws AuthService.UserNotFoundException,
            JwtService.TokenInvalidException, JwtService.TokenExpiredException {
        final var jwtPayload = jwtService.extractPayload(jwtToken);
        return Optional.of(authService.loadUserByEmail(jwtPayload.email()))
                .map(it -> new UserInfo(it.userId(), it.email()))
                .orElseThrow();
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var tokenOptional = extractTokenFromRequest(request);
                if (tokenOptional.isPresent()) {
                    UserInfo userInfo;
                    String bearerToken = tokenOptional.get();
                    if (isApiToken(bearerToken)) {
                        userInfo = authenticateByApiToken(bearerToken);
                    } else {
                        userInfo = authenticationByJwtToken(bearerToken);
                    }
                    final var context = SecurityContextHolder.createEmptyContext();
                    final var authenticationToken = new UsernamePasswordAuthenticationToken(
                            userInfo,
                            null,
                            userInfo.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (JwtService.TokenExpiredException | JwtService.TokenInvalidException |
                 AuthService.UserNotFoundException | ApiTokenService.InvalidTokenException e) {
            log.warn("Authentication exception occurred: [exception: {}]", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
