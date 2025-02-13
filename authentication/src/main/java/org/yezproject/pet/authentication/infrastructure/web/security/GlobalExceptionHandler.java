package org.yezproject.pet.authentication.infrastructure.web.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.yezproject.pet.authentication.infrastructure.web.exception.BadRequestException;
import org.yezproject.pet.authentication.infrastructure.web.exception.GlobalResourceAccessPermissionException;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler implements AuthenticationEntryPoint {
    @ExceptionHandler(GlobalResourceAccessPermissionException.class)
    void denyResourceAccess(HttpServletResponse response, GlobalResourceAccessPermissionException e) {
        log.warn("GlobalResourceAccessPermissionException Occurred", e);
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(BadRequestException.class)
    void badRequestBody(HttpServletResponse response, BadRequestException e) {
        log.warn("BadRequestException Occurred", e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NullPointerException.class)
    void internalError(HttpServletResponse response, NullPointerException e) {
        log.warn("NullPointerException Occurred", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    void expiredToken(HttpServletResponse response, ExpiredJwtException e) {
        log.warn("ExpiredJwtException Occurred", e);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        log.warn("AuthenticationException Occurred");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
