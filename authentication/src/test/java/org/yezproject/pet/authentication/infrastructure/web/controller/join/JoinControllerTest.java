package org.yezproject.pet.authentication.infrastructure.web.controller.join;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.yezproject.pet.authentication.application.user.driven.JoinService;
import org.yezproject.pet.authentication.application.user.driven.SignUpDto;
import org.yezproject.pet.authentication.infrastructure.web.controller.BaseControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yezproject.pet.test_common.RandomUtils.randomShortString;

@WebMvcTest(value = JoinController.class)
class JoinControllerTest extends BaseControllerTest {
    private static ObjectMapper objectMapper;
    private static SignUpDto signUpReq;

    @MockBean
    private JoinService joinService;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        signUpReq = new SignUpDto(
                randomShortString(),
                randomShortString(),
                randomShortString()
        );
    }

    @Test
    void signup_return_201() throws Exception {
        when(joinService.signup(any())).thenReturn(randomShortString());
        this.mockMvc.perform(post("/public/sign_up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andExpect(status().isCreated());
    }

    @Test
    void signup_return_409() throws Exception {
        when(joinService.signup(any())).thenThrow(JoinService.UserExistedException.class);
        this.mockMvc.perform(post("/public/sign_up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(signUpReq)))
                .andExpect(status().isConflict());
    }
}
