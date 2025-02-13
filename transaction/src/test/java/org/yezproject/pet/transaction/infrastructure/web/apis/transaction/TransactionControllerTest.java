package org.yezproject.pet.transaction.infrastructure.web.apis.transaction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.yezproject.pet.test_common.RandomUtils;
import org.yezproject.pet.transaction.application.transaction.driven.CreateTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.ModifyTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.RetrieveTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.TransactionService;
import org.yezproject.pet.transaction.infrastructure.web.apis.BaseControllerTest;

import java.util.List;
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
import static org.yezproject.pet.test_common.RandomUtils.*;

@WebMvcTest(value = TransactionController.class)
class TransactionControllerTest extends BaseControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    TransactionService transactionService;

    @Test
    void retrieve_return_200() throws Exception {
        final var transactionId = randomShortString();
        final var transactionDTO = randomRetrieveTransactionDTO().get();
        when(this.transactionService.retrieve(anyString(), anyString())).thenReturn(transactionDTO);

        final var result = this.mockMvc.perform(get("/transactions/" + transactionId)
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
                .returns(transactionDTO.name(), RetrieveTransactionResponse::name)
                .returns(transactionDTO.amount(), RetrieveTransactionResponse::amount)
                .returns(transactionDTO.currency().name(), res -> res.currency().name())
                .returns(transactionDTO.currency().symbol(), res -> res.currency().symbol())
                .returns(transactionDTO.transactionDate().toEpochMilli(), RetrieveTransactionResponse::transactionDate);
    }

    @Test
    void retrieve_return_404() throws Exception {
        final var transactionId = randomShortString();
        when(this.transactionService.retrieve(anyString(), anyString()))
                .thenThrow(TransactionService.TransactionNotExistedException.class);

        this.mockMvc.perform(get("/transactions/" + transactionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void retrieveAll_return_200() throws Exception {
        final var transactionDTOs = randomShortList(randomRetrieveTransactionDTO());
        when(this.transactionService.retrieveAll(any())).thenReturn(transactionDTOs);

        final var result = this.mockMvc.perform(get("/transactions")
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
                        .returns(transactionDTOs.get(index).name(), RetrieveTransactionResponse::name)
                        .returns(transactionDTOs.get(index).amount(), RetrieveTransactionResponse::amount)
                        .returns(transactionDTOs.get(index).currency().name(), res -> res.currency().name())
                        .returns(transactionDTOs.get(index).currency().symbol(), res -> res.currency().symbol())
                        .returns(transactionDTOs.get(index).transactionDate().toEpochMilli(), RetrieveTransactionResponse::transactionDate)
                );
    }

    @Test
    void create_return_201() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
                randomInstant().toEpochMilli()
        );

        final var randomId = randomShortString();
        when(transactionService.create(any()))
                .thenReturn(randomId);

        this.mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isCreated());

        then(transactionService).should().create(
                new CreateTransactionDto(
                        mockUser.id(),
                        requestBody.categoryId(),
                        requestBody.name(),
                        requestBody.amount(),
                        requestBody.transactionDate()
                )
        );
    }

    // TODO: close temporally
    /*@Test
    void create_return_400() throws Exception {
        final var requestBody = new CreateTransactionRequest(
                randomShortString(),
                randomShortString(),
                null,
                randomCurrency(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void modify_return_204() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
                randomInstant().toEpochMilli()
        );
        final var requestModifyTransactionId = randomShortString();

        this.mockMvc.perform(put("/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());

        then(this.transactionService).should().modify(
                new ModifyTransactionDto(
                        mockUser.id(),
                        requestModifyTransactionId,
                        requestBody.categoryId(),
                        requestBody.name(),
                        requestBody.amount(),
                        requestBody.transactionDate()
                )
        );
    }

    @Test
    void modify_return_404() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
                randomInstant().toEpochMilli()
        );
        final var requestModifyTransactionId = randomShortString();

        doThrow(TransactionService.TransactionNotExistedException.class).when(this.transactionService)
                .modify(any());

        this.mockMvc.perform(put("/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void modify_return_400() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
                randomInstant().toEpochMilli()
        );
        final var requestModifyTransactionId = randomShortString();

        doThrow(TransactionService.TransactionInvalidModifyException.class).when(this.transactionService)
                .modify(any());

        this.mockMvc.perform(put("/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modify_missing_transaction_id_return_404() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                randomShortString(),
                randomPositiveDouble(),
                randomInstant().toEpochMilli()
        );

        this.mockMvc.perform(put("/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());
    }

    // TODO: close temporally
    /*@Test
    void modify_return_400() throws Exception {
        final var requestBody = new ModifyTransactionRequest(
                randomShortString(),
                "",
                randomPositiveDouble(),
                randomCurrency(),
                randomInstant().toEpochMilli()
        );
        final var requestModifyTransactionId = randomShortString();

        this.mockMvc.perform(put("/transactions/" + requestModifyTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void delete_return_204() throws Exception {
        String[] ids = randomShortList(RandomUtils::randomShortString).toArray(String[]::new);
        this.mockMvc.perform(delete("/transactions")
                        .param("ids", ids)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());
    }

    private Supplier<RetrieveTransactionDto> randomRetrieveTransactionDTO() {
        return () -> new RetrieveTransactionDto(
                randomShortString(),
                randomShortString(),
                randomShortString(),
                randomNegativeDouble(),
                new RetrieveTransactionDto.RetrieveTransactionCurrencyDTO(
                        "VND",
                        "₫"
                ),
                randomInstant()
        );
    }
}
