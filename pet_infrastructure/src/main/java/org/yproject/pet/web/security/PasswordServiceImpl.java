package org.yproject.pet.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yproject.pet.user.driving.PasswordService;

@Component
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean matches(String raw, String encodedPassword) {
        return passwordEncoder.matches(raw, encodedPassword);
    }

    @Override
    public String encode(String raw) {
        return passwordEncoder.encode(raw);
    }
}
