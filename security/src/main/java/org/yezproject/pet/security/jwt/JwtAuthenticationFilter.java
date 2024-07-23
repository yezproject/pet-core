package org.yezproject.pet.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("start JwtAuthenticationFilter");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var tokenOptional = extractTokenFromRequest(request);
            if (tokenOptional.isPresent() && !isApiToken(tokenOptional.get())) {
                var token = tokenOptional.get();
                var context = SecurityContextHolder.createEmptyContext();
                var authentication = authenticationManager.authenticate(new JwtAuthentication(token));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
