package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yezproject.pet.authentication.application.user.driving.PasswordService;

@Component
record PasswordServiceImpl(
        PasswordEncoder passwordEncoder
) implements PasswordService {

    @Override
    public boolean matches(String raw, String encodedPassword) {
        return passwordEncoder.matches(raw, encodedPassword);
    }

    @Override
    public String encode(String raw) {
        return passwordEncoder.encode(raw);
    }
}
