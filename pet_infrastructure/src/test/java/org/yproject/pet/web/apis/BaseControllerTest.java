package org.yproject.pet.web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.yproject.pet.jwt.JwtService;
import org.yproject.pet.user.AuthInfo;
import org.yproject.pet.user.AuthService;
import org.yproject.pet.user.UserDao;
import org.yproject.pet.web.security.SecurityConfig;

import java.util.Collections;

import static org.yproject.pet.RandomUtils.randomShortString;

@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
    @Autowired
    public WebApplicationContext webApplicationContext;
    @Autowired
    public MockMvc mockMvc;

    /* Require for Security Auth */
    @MockBean
    public UserDao userDAO;
    @MockBean
    public JwtService jwtService;
    @MockBean
    public AuthService authService;

    protected final AuthInfo mockUser = randomUser();
    protected final AuthInfo mockAdmin = randomAdmin();

    private static AuthInfo randomUser() {
        return new AuthInfo(
                randomShortString(),
                randomShortString(),
                Collections.emptySet()
        );
    }

    private static AuthInfo randomAdmin() {
        return new AuthInfo(
                randomShortString(),
                randomShortString(),
                Collections.emptySet()
        );
    }
}
