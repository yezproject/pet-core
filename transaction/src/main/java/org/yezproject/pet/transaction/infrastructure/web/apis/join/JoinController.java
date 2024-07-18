package org.yezproject.pet.transaction.infrastructure.web.apis.join;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.transaction.application.user.driven.JoinService;
import org.yezproject.pet.transaction.application.user.driven.SignUpApplicationDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
@Tag(name = "User", description = "Join System APIs")
class JoinController {
    private final JoinService joinService;

    private static SignInResponse toSignInResponse(String token) {
        return new SignInResponse(token);
    }

    private static SignUpResponse toSignUpResponse(String userId) {
        return new SignUpResponse(userId);
    }

    private static SignUpApplicationDto toSignUpApplicationDto(SignUpRequest request) {
        return new SignUpApplicationDto(
                request.fullName(),
                request.email(),
                request.password()
        );
    }

    @PostMapping("/sign_in")
    @ResponseStatus(HttpStatus.OK)
    SignInResponse signIn(
            @RequestBody SignInRequest request
    ) {
        final var token = joinService.signIn(
                request.email(),
                request.password()
        );
        return JoinController.toSignInResponse(token);
    }

    @PostMapping("/sign_up")
    @ResponseStatus(HttpStatus.CREATED)
    SignUpResponse signUp(
            @RequestBody SignUpRequest request
    ) {
        final var userId = joinService.signup(
                toSignUpApplicationDto(request)
        );
        return JoinController.toSignUpResponse(userId);
    }

    @ExceptionHandler(JoinService.UserNotFoundException.class)
    ResponseEntity<String> userNotFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("USER_NOT_FOUND");
    }

    @ExceptionHandler(JoinService.InvalidPasswordException.class)
    ResponseEntity<String> invalidPasswordExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID_PASSWORD");
    }

    @ExceptionHandler(JoinService.UserExistedException.class)
    ResponseEntity<String> userExistedExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EMAIL EXISTED");
    }
}
