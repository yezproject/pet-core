package org.yproject.pet.core.infrastructure.web.apis.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.yproject.pet.core.application.transaction.CreateTransactionDTO;
import org.yproject.pet.core.application.transaction.ModifyTransactionDTO;
import org.yproject.pet.core.application.transaction.RetrieveTransactionDTO;
import org.yproject.pet.core.application.transaction.TransactionService;
import org.yproject.pet.core.domain.api_token.entities.ApiToken;
import org.yproject.pet.core.domain.transaction.enums.Currency;
import org.yproject.pet.core.domain.user.entities.User;
import org.yproject.pet.core.infrastructure.web.apis.BaseControllerTest;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;
import org.yproject.pet.core.util.RandomUtils;
import org.yproject.pet.core.util.TransactionRandomUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private final Set<ApiToken> mockTokenSet = Collections.emptySet();

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    void authSetup() {
        when(this.jwtService.extractEmail(any()))
                .thenReturn(randomShortString());
        when(this.jwtService.isTokenValid(any(), any()))
                .thenReturn(true);
        when(this.userInfoService.loadUserByEmail(any()))
                .thenReturn(new UserInfo(mockUser, mockTokenSet));
    }

    @Test
    void retrieve_return_200() throws Exception {
        final var transactionId = randomShortString();
        final var transaction = TransactionRandomUtils.randomTransaction();
        final var transactionDTO = RetrieveTransactionDTO.fromDomain(transaction);
        when(this.transactionService.retrieve(anyString(), anyString())).thenReturn(transactionDTO);

        this.mockMvc.perform(get("/api/transactions/" + transactionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(transactionDTO.id())))
                .andExpect(jsonPath("$.description", is(transactionDTO.description())))
                .andExpect(jsonPath("$.amount", comparesEqualTo(BigDecimal.valueOf(transactionDTO.amount()))))
                .andExpect(jsonPath("$.currency.name", is(transactionDTO.currency().name())))
                .andExpect(jsonPath("$.currency.symbol", is(transactionDTO.currency().getSymbol())))
                .andExpect(jsonPath("$.createTime", is(transactionDTO.createTime().toEpochMilli())));
    }

    @Test
    void retrieve_return_404() throws Exception {
        final var transactionId = randomShortString();
        when(this.transactionService.retrieve(anyString(), anyString()))
                .thenThrow(TransactionService.TransactionNotExisted.class);

        this.mockMvc.perform(get("/api/transactions/" + transactionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void retrieveAll_return_200() throws Exception {
        final var transactions = randomShortList(TransactionRandomUtils::randomTransaction);
        final var transactionDTOs = transactions.stream().map(RetrieveTransactionDTO::fromDomain).toList();
        when(this.transactionService.retrieveAll(any())).thenReturn(transactionDTOs);

        this.mockMvc.perform(get("/api/transactions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(transactions.get(0).getId().value())))
                .andExpect(jsonPath("$[0].description", is(transactions.get(0).getDescription())))
                .andExpect(jsonPath("$[0].amount", comparesEqualTo(BigDecimal.valueOf(transactions.get(0).getAmount()))))
                .andExpect(jsonPath("$[0].currency.name", is(transactions.get(0).getCurrency().name())))
                .andExpect(jsonPath("$[0].currency.symbol", is(transactions.get(0).getCurrency().getSymbol())))
                .andExpect(jsonPath("$[0].createTime", is(transactions.get(0).getCreateTime().toEpochMilli())));
    }

    @Test
    void create_return_201() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        final var randomId = randomShortString();
        when(transactionService.create(anyString(), any()))
                .thenReturn(randomId);

        this.mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isCreated());

        then(transactionService).should().create(
                mockUser.getId().value(),
                new CreateTransactionDTO(
                        requestBody.categoryId(),
                        requestBody.description(),
                        requestBody.amount(),
                        requestBody.currency(),
                        requestBody.createTime()
                )
        );
    }

    @Test
    void create_return_400() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
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
        final var requestModifyTransactionId = randomShortString();

        this.mockMvc.perform(put("/api/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());

        then(this.transactionService).should().modify(
                mockUser.getId().value(),
                requestModifyTransactionId,
                new ModifyTransactionDTO(
                        requestBody.categoryId(),
                        requestBody.description(),
                        requestBody.amount(),
                        requestBody.currency(),
                        requestBody.createTime()
                )
        );
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
        final var requestModifyTransactionId = randomShortString();

        doThrow(TransactionService.TransactionNotExisted.class).when(this.transactionService)
                .modify(anyString(), anyString(), any());

        this.mockMvc.perform(put("/api/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void modify_missing_transaction_id_return_404() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomDouble(),
                randomFrom(Currency.values()).name(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(put("/api/transactions/")
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
        final var requestModifyTransactionId = randomShortString();

        this.mockMvc.perform(put("/api/transactions/" + requestModifyTransactionId)
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
