package org.yezproject.pet.authentication.infrastructure.web.controller.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;

@RestController
@RequestMapping("/internal/tokens")
@RequiredArgsConstructor
class InternalTokenAuthController {
    private final ApiTokenAuthenticationService apiTokenAuthenticationService;

    @GetMapping("/{token}")
    PetUserDetails getAuthByToken(@PathVariable(name = "token") String token) {
        return apiTokenAuthenticationService.authenticate(token);
    }
}
