package org.yproject.pet.web.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.yproject.pet.jwt.JwtService;
import org.yproject.pet.security.UserInfoService;
import org.yproject.pet.user.UserStorage;
import org.yproject.pet.user.entities.User;
import org.yproject.pet.user.entities.UserBuilder;
import org.yproject.pet.user.enums.ApprovalStatus;
import org.yproject.pet.user.enums.Role;
import org.yproject.pet.web.security.SecurityConfig;

import static org.yproject.pet.RandomUtils.randomInstant;
import static org.yproject.pet.RandomUtils.randomShortString;

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

    protected final User mockUser = randomUser();
    protected final User mockAdmin = randomAdmin();

    private static User randomUser() {
        return new UserBuilder(randomShortString())
                .email(randomShortString())
                .fullName(randomShortString())
                .password(randomShortString())
                .role(Role.USER)
                .approvalStatus(ApprovalStatus.APPROVED)
                .createAt(randomInstant())
                .approvedAt(randomInstant())
                .build();
    }

    private static User randomAdmin() {
        return new UserBuilder(randomShortString())
                .email(randomShortString())
                .fullName(randomShortString())
                .password(randomShortString())
                .role(Role.ADMIN)
                .approvalStatus(ApprovalStatus.APPROVED)
                .createAt(randomInstant())
                .approvedAt(randomInstant())
                .build();
    }
}
