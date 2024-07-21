package org.yezproject.pet.transaction.infrastructure.web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.yezproject.pet.transaction.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.transaction.application.jwt.JwtService;
import org.yezproject.pet.transaction.application.user.driven.AuthInfo;
import org.yezproject.pet.transaction.application.user.driven.AuthService;
import org.yezproject.pet.transaction.application.user.driving.UserRepository;
import org.yezproject.pet.transaction.infrastructure.web.security.SecurityConfig;

import static org.yezproject.pet.test_common.RandomUtils.randomShortString;

@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
    @Autowired
    public WebApplicationContext webApplicationContext;
    @Autowired
    public MockMvc mockMvc;

    /* Require for Security Auth */
    @MockBean
    public UserRepository userRepository;
    @MockBean
    public JwtService jwtService;
    @MockBean
    public AuthService authService;
    @MockBean
    public ApiTokenService apiTokenService;

    protected final AuthInfo mockUser = randomUser();
    protected final AuthInfo mockAdmin = randomAdmin();

    private static AuthInfo randomUser() {
        return new AuthInfo(
                randomShortString(),
                randomShortString()
        );
    }

    private static AuthInfo randomAdmin() {
        return new AuthInfo(
                randomShortString(),
                randomShortString()
        );
    }
}