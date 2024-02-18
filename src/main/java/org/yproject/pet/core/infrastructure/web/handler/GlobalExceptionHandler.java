package org.yproject.pet.core.infrastructure.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.yproject.pet.core.domain.exception.DomainException;

@ControllerAdvice
class GlobalExceptionHandler implements AuthenticationEntryPoint {
    @ExceptionHandler(DomainException.class)
    void denyOnDomainFail(HttpServletResponse response) {
        response.setStatus(HttpStatus.I_AM_A_TEAPOT.value());
    }

    @ExceptionHandler(GlobalResourceAccessPermissionException.class)
    void denyResourceAccess(HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
