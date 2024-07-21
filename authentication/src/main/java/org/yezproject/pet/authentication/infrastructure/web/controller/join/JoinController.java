package org.yezproject.pet.authentication.infrastructure.web.controller.join;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.authentication.application.user.driven.JoinService;
import org.yezproject.pet.authentication.application.user.driven.SignInDto;
import org.yezproject.pet.authentication.application.user.driven.SignUpDto;
import org.yezproject.pet.security.jwt.JwtHelper;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
class JoinController {
    private final JoinService joinService;
    private final JwtHelper jwtHelper;

    @PostMapping("/sign_in")
    @ResponseStatus(HttpStatus.OK)
    SignInResponse signIn(
            @RequestBody SignInDto request
    ) {
        final var userDto = joinService.signIn(request);
        final var claims = new HashMap<String, Object>();
        claims.put("name", userDto.fullName());
        claims.put("uid", userDto.userId());
        var token = jwtHelper.generateToken(claims, userDto.email());
        return new SignInResponse(token);
    }

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    SignUpResponse signUp(
            @RequestBody SignUpDto request
    ) {
        final var userId = joinService.signup(request);
        return new SignUpResponse(userId);
    }

    @ExceptionHandler(JoinService.UserNotFoundException.class)
    ResponseEntity<String> userNotFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(JoinService.InvalidPasswordException.class)
    ResponseEntity<String> invalidPasswordExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(JoinService.UserExistedException.class)
    ResponseEntity<Void> userExistedExceptionHandler() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
