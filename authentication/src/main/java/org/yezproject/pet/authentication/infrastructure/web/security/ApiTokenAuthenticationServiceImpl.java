package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.stereotype.Component;
import org.yezproject.pet.authentication.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;

@Component
record ApiTokenAuthenticationServiceImpl(
        ApiTokenService apiTokenService
) implements ApiTokenAuthenticationService {
    @Override
    public PetUserDetails authenticate(String token) {
        try {
            var userDto = apiTokenService.verify(token);
            return new PetUserDetails(userDto.userId(), userDto.email());
        } catch (ApiTokenService.InvalidTokenException e) {
            return null;
        }
    }
}
