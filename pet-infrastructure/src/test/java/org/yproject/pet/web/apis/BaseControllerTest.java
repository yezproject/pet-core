package org.yproject.pet.web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.yproject.pet.jwt.JwtService;
import org.yproject.pet.security.UserInfoService;
import org.yproject.pet.user.UserStorage;
import org.yproject.pet.web.security.SecurityConfig;

@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
    @Autowired
    public WebApplicationContext webApplicationContext;
    @Autowired
    public MockMvc mockMvc;

    /* Require for Security Auth */
    @MockBean
    public UserStorage userStorage;
    @MockBean
    public JwtService jwtService;
    @MockBean
    public UserInfoService userInfoService;

}
