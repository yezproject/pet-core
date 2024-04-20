package org.yproject.pet.web.apis.transaction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.yproject.pet.RandomUtils;
import org.yproject.pet.api_token.entities.ApiToken;
import org.yproject.pet.transaction.CreateTransactionDTO;
import org.yproject.pet.transaction.ModifyTransactionDTO;
import org.yproject.pet.transaction.RetrieveTransactionDTO;
import org.yproject.pet.transaction.TransactionService;
import org.yproject.pet.transaction.enums.Currency;
import org.yproject.pet.web.apis.BaseControllerTest;
import org.yproject.pet.web.security.UserInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yproject.pet.RandomUtils.*;

@WebMvcTest(value = TransactionController.class)
class TransactionControllerTest extends BaseControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<ApiToken> mockTokenSet = Collections.emptySet();

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    void authSetup() {
        when(this.jwtService.extractEmail(any()))
                .thenReturn(randomShortString());
        when(this.jwtService.isTokenValid(any(), any(), any()))
                .thenReturn(true);
        when(this.userInfoService.loadUserByEmail(any()))
                .thenReturn(new UserInfo(mockUser, mockTokenSet));
    }

    @Test
    void retrieve_return_200() throws Exception {
        final var transactionId = randomShortString();
        final var transactionDTO = randomRetrieveTransactionDTO().get();
        when(this.transactionService.retrieve(anyString(), anyString())).thenReturn(transactionDTO);

        final var result = this.mockMvc.perform(get("/api/transactions/" + transactionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andReturn();
        final var response = this.objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                RetrieveTransactionResponse.class
        );

        assertThat(response)
                .returns(transactionDTO.id(), RetrieveTransactionResponse::id)
                .returns(transactionDTO.categoryId(), RetrieveTransactionResponse::categoryId)
                .returns(transactionDTO.description(), RetrieveTransactionResponse::description)
                .returns(transactionDTO.amount(), RetrieveTransactionResponse::amount)
                .returns(transactionDTO.currency().name(), res -> res.currency().name())
                .returns(transactionDTO.currency().symbol(), res -> res.currency().symbol())
                .returns(transactionDTO.createTime().toEpochMilli(), RetrieveTransactionResponse::createTime);
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
        final var transactionDTOs = randomShortList(randomRetrieveTransactionDTO());
        when(this.transactionService.retrieveAll(any())).thenReturn(transactionDTOs);

        final var result = this.mockMvc.perform(get("/api/transactions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andReturn();
        final var responses = this.objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<List<RetrieveTransactionResponse>>() {
                }
        );

        IntStream.range(0, responses.size())
                .forEach(index -> assertThat(responses.get(index))
                        .returns(transactionDTOs.get(index).id(), RetrieveTransactionResponse::id)
                        .returns(transactionDTOs.get(index).categoryId(), RetrieveTransactionResponse::categoryId)
                        .returns(transactionDTOs.get(index).description(), RetrieveTransactionResponse::description)
                        .returns(transactionDTOs.get(index).amount(), RetrieveTransactionResponse::amount)
                        .returns(transactionDTOs.get(index).currency().name(), res -> res.currency().name())
                        .returns(transactionDTOs.get(index).currency().symbol(), res -> res.currency().symbol())
                        .returns(transactionDTOs.get(index).createTime().toEpochMilli(), RetrieveTransactionResponse::createTime)
                );
    }

    @Test
    void create_return_201() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
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
                randomPositiveDouble(),
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
                randomPositiveDouble(),
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
                randomPositiveDouble(),
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
                randomPositiveDouble(),
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

    private Supplier<RetrieveTransactionDTO> randomRetrieveTransactionDTO() {
        return () -> new RetrieveTransactionDTO(
                randomShortString(),
                randomShortString(),
                randomShortString(),
                randomNegativeDouble(),
                new RetrieveTransactionDTO.RetrieveTransactionCurrencyDTO(
                        "VND",
                        "₫"
                ),
                randomInstant()
        );
    }
}
