package org.yezproject.pet.authentication.infrastructure.web.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.authentication.application.user.driven.UserQuery;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class InternalAuthController {
    private final UserQuery userQuery;
    private final ApiTokenAuthenticationService apiTokenAuthenticationService;

    @GetMapping("/user/{user_email}")
    PetUserDetails getAuthByEmail(@PathVariable(name = "user_email") String userEmail) {
        final var userInfo = userQuery.getByEmail(userEmail);
        return new PetUserDetails(userInfo.userId(), userInfo.email());
    }

    @GetMapping("/token/{token}")
    PetUserDetails getAuthByToken(@PathVariable(name = "token") String token) {
        return apiTokenAuthenticationService.authenticate(token);
    }

    @ExceptionHandler(UserQuery.UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void userNotFound() {
    }
}
