package org.yezproject.pet.web.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yezproject.pet.jwt.JwtService;
import org.yezproject.pet.web.security.UserInfo;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService authService;

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            final var token = bearerToken.substring(7);
            return Optional.of(token);
        }
        return Optional.empty();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final var tokenOptional = extractTokenFromRequest(request);
            if (SecurityContextHolder.getContext().getAuthentication() == null && tokenOptional.isPresent()) {
                final var jwtToken = tokenOptional.get();
                final var email = jwtService.extractEmail(jwtToken);
                if (StringUtils.isNotEmpty(email)) {
                    final UserInfo userInfo;
                    userInfo = (UserInfo) authService.loadUserByUsername(email);
                    if (jwtService.isTokenValid(jwtToken, userInfo.email())) {
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
            }
        } catch (JwtService.TokenExpiredException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        filterChain.doFilter(request, response);
    }
}
