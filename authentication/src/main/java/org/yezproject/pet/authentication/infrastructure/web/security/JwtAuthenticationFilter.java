package org.yezproject.pet.authentication.infrastructure.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yezproject.pet.security.PetUserDetails;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("start JwtAuthenticationFilter");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var context = SecurityContextHolder.createEmptyContext();
                final var uid = request.getHeader("X-User-ID");
                final var subject = request.getHeader("X-User-Email");
                PetUserDetails userDetails = new PetUserDetails(uid, subject);
                var authentication =  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
