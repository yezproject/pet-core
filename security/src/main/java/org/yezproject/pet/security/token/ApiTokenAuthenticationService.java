package org.yezproject.pet.security.token;

import org.yezproject.pet.security.PetUserDetails;

public interface ApiTokenAuthenticationService {
    PetUserDetails authenticate(String token);
}
