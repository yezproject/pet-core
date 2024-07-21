package org.yezproject.pet.security.token;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.yezproject.pet.security.PetUserDetails;

public record ApiTokenAuthenticationProvider(
        ApiTokenAuthenticationService authenticationService
) implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        if (token != null) {
            PetUserDetails userDetails = authenticationService.authenticate(token);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        ApiTokenAuthentication.class.isAssignableFrom(authentication);
        return authentication.equals(ApiTokenAuthentication.class);
    }
}
