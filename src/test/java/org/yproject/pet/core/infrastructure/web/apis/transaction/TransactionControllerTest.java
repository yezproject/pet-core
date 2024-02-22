package org.yproject.pet.core.infrastructure.web.apis.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.yproject.pet.core.application.transaction.RetrieveTransactionDto;
import org.yproject.pet.core.application.transaction.TransactionService;
import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.user.User;
import org.yproject.pet.core.infrastructure.web.apis.BaseControllerTest;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;
import org.yproject.pet.core.util.RandomUtils;
import org.yproject.pet.core.util.TransactionRandomUtils;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yproject.pet.core.util.RandomUtils.*;
import static org.yproject.pet.core.util.UserRandomUtils.randomUser;


@WebMvcTest(value = TransactionController.class)
class TransactionControllerTest extends BaseControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final User mockUser = randomUser();

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    void authSetup() {
        when(this.jwtService.extractEmail(any()))
                .thenReturn(randomShortString());
        when(this.jwtService.isTokenValid(any(), any()))
                .thenReturn(true);
        when(this.userInfoService.loadUserByEmail(any()))
                .thenReturn(new UserInfo(mockUser));
    }

    @Test
    void retrieveAll_return_200() throws Exception {
        final var transactions = randomShortList(TransactionRandomUtils::randomTransaction);
        final var transactionDTOs = transactions.stream().map(RetrieveTransactionDto::fromDomain).toList();
        when(this.transactionService.retrieveAll(any())).thenReturn(transactionDTOs);

        this.mockMvc.perform(get("/api/transactions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(transactions.get(0).id())))
                .andExpect(jsonPath("$[0].description", is(transactions.get(0).description())))
                .andExpect(jsonPath("$[0].amount", is(transactions.get(0).amount().doubleValue())))
                .andExpect(jsonPath("$[0].currency", is(transactions.get(0).currency().toString())))
                .andExpect(jsonPath("$[0].createTime", is(transactions.get(0).createTime().toEpochMilli())));
    }

    @Test
    void create_return_201() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        final var randomId = randomShortString();
        when(transactionService.create(anyString(), anyString(), anyDouble(), anyString(), anyLong()))
                .thenReturn(randomId);

        this.mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isCreated());

        then(transactionService).should().create(
                mockUser.id(),
                requestBody.description(),
                requestBody.amount(),
                requestBody.currency(),
                requestBody.createTime());
    }

    @Test
    void create_return_400() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                null,
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());

    }

    @Test
    void modify_return_204() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(put("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());

        then(this.transactionService).should().modify(
                mockUser.id(),
                requestBody.id(),
                requestBody.description(),
                requestBody.amount(),
                requestBody.currency(),
                requestBody.createTime());
    }

    @Test
    void modify_return_404() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );


        doThrow(TransactionService.TransactionNotExisted.class).when(this.transactionService)
                .modify(anyString(), anyString(), anyString(), anyDouble(), anyString(), anyLong());

        this.mockMvc.perform(put("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void modify_return_400() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                "",
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(put("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_return_204() throws Exception {
        String[] ids = randomShortList(RandomUtils::randomShortString).toArray(String[]::new);
        this.mockMvc.perform(delete("/api/transactions")
                        .param("ids", ids)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_return_400() throws Exception {
        this.mockMvc.perform(delete("/api/transactions?ids=")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());
    }
}
