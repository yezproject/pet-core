package org.yezproject.pet.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yezproject.pet.api_token.driven.ApiTokenService;
import org.yezproject.pet.user.driven.AuthService;
import org.yezproject.pet.web.security.UserInfo;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiTokenAuthenticationFilter extends OncePerRequestFilter {
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

    private boolean isApiToken(String token) {
        return token.startsWith("yzp_");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final var tokenOptional = extractTokenFromRequest(request);
        if (SecurityContextHolder.getContext().getAuthentication() == null && tokenOptional.isPresent()) {
            final var apiToken = tokenOptional.get();
            if (isApiToken(apiToken)) {
                try {
                    final var userId = apiTokenService.verify(apiToken).userId();
                    final UserInfo userInfo = Optional.of(authService.loadUserById(userId))
                            .map(it -> new UserInfo(it.userId(), it.email()))
                            .orElseThrow();
                    final var context = SecurityContextHolder.createEmptyContext();
                    final var authenticationToken = new UsernamePasswordAuthenticationToken(
                            userInfo,
                            null,
                            userInfo.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                } catch (ApiTokenService.InvalidTokenException | AuthService.UserNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
