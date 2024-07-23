package org.yezproject.pet.transaction.infrastructure.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;

@Component
public record ApiTokenAuthenticationServiceImpl() implements ApiTokenAuthenticationService {

    @Override
    public PetUserDetails authenticate(String token) {
        var restTemplate = new RestTemplate();
        String url = "localhost:8081" + "/auth/token/" + token;
        return restTemplate.getForObject(url, PetUserDetails.class);
    }
}
