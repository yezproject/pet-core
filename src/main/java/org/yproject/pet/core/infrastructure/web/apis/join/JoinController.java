package org.yproject.pet.core.infrastructure.web.apis.join;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.core.application.join.JoinService;
import org.yproject.pet.core.application.join.SignUpApplicationDto;

@RestController
@RequestMapping("/auth")
@Tag(name = "User", description = "Join System APIs")
record JoinController(
        JoinService joinService
) {
    @PostMapping("/sign_in")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Sign In")
    @ApiResponse(responseCode = "200", description = "Sign in success", useReturnTypeSchema = true)
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
    @Operation(summary = "Sign Up")
    @ApiResponse(responseCode = "200", description = "Sign up success", useReturnTypeSchema = true)
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
    ResponseEntity<Void> userExistedExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

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
}
