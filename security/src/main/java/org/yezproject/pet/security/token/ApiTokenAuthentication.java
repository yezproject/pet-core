package org.yezproject.pet.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public final class ApiTokenAuthentication extends AbstractAuthenticationToken {
    private final String token;

    public ApiTokenAuthentication(String token) {
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
