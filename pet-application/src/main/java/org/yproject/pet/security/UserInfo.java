package org.yproject.pet.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yproject.pet.api_token.entities.ApiToken;
import org.yproject.pet.user.entities.User;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public record UserInfo(
        User user,
        Set<ApiToken> apiTokenSet
) implements UserDetails {

    @Serial
    private static final long serialVersionUID = 5568817527711833925L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    public String getId() {
        return user.getId().value();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
