package org.yezproject.pet.transaction.infrastructure.web.apis;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;
import org.yezproject.pet.transaction.infrastructure.web.security.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.yezproject.pet.test_common.RandomUtils.randomShortString;

@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
    @Autowired
    public WebApplicationContext webApplicationContext;
    @Autowired
    public MockMvc mockMvc;
    public PetUserDetails mockUser = new PetUserDetails(randomShortString(), randomShortString());

    @MockBean
    public ApiTokenAuthenticationService apiTokenAuthenticationService;
    @MockBean
    public AuthenticationManager authenticationManager;


    @BeforeEach
    void authSetup() {
        when(this.authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities()));
    }
}
