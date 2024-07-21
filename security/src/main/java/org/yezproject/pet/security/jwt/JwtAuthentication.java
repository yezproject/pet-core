package org.yezproject.pet.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public final class JwtAuthentication extends AbstractAuthenticationToken {
    private final String token;

    public JwtAuthentication(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String getPrincipal() {
        return null;
    }
}
