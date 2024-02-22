package org.yproject.pet.core.infrastructure.web.apis.join;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.yproject.pet.core.application.join.JoinService;
import org.yproject.pet.core.infrastructure.web.apis.BaseControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yproject.pet.core.util.RandomUtils.randomShortString;

@WebMvcTest(value = JoinController.class)
class JoinControllerTest extends BaseControllerTest {
    private static ObjectMapper objectMapper;
    private static SignUpRequest signUpReq;

    @MockBean
    private JoinService joinService;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        signUpReq = new SignUpRequest(
                randomShortString(),
                randomShortString(),
                randomShortString()
        );
    }

    @Test
    void signup_return_201() throws Exception {
        when(joinService.signup(any())).thenReturn(randomShortString());
        this.mockMvc.perform(post("/auth/sign_up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andExpect(status().isCreated());
    }

    @Test
    void signup_return_400() throws Exception {
        when(joinService.signup(any())).thenThrow(JoinService.UserNotFoundException.class);
        this.mockMvc.perform(post("/auth/sign_up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andExpect(status().isBadRequest());
    }
}
