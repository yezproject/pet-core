package org.yezproject.pet.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.yezproject.pet.security.PetUserDetails;

public record JwtAuthenticationProvider(
        JwtHelper jwlHelper,
        UserDetailsService userDetailsService
) implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        if (token != null) {
            try {
                JwtDetails jwtDetails = jwlHelper().extractPayload(token);
                PetUserDetails userDetails = new PetUserDetails(jwtDetails.uid(), jwtDetails.subject());
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            } catch (TokenExpiredException | TokenInvalidException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        JwtAuthentication.class.isAssignableFrom(authentication);
        return authentication.equals(JwtAuthentication.class);
    }
}