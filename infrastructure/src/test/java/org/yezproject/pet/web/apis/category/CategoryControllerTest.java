package org.yezproject.pet.web.apis.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.yezproject.pet.category.driven.CategoryService;
import org.yezproject.pet.category.driven.RetrieveCategoryDto;
import org.yezproject.pet.user.driven.AuthService;
import org.yezproject.pet.web.apis.BaseControllerTest;

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
import static org.yezproject.pet.RandomUtils.randomShortList;
import static org.yezproject.pet.RandomUtils.randomShortString;

@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest extends BaseControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void authSetup() throws AuthService.UserNotFoundException {
        when(this.jwtService.extractEmail(any()))
                .thenReturn(randomShortString());
        when(this.jwtService.isTokenValid(any(), any()))
                .thenReturn(true);
        when(this.authService.loadUserByEmail(any()))
                .thenReturn(this.mockUser);
    }

    @Test
    void retrieve_all_return_200() throws Exception {
        final var categoryDTOs = randomShortList(randomCategoryDTO());
        when(categoryService.retrieveAll(anyString())).thenReturn(categoryDTOs);

        final var result = this.mockMvc.perform(get("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isOk())
                .andReturn();
        final var responses = this.objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<List<RetrieveCategoryResponse>>() {
                }
        );
        IntStream.range(0, responses.size())
                .forEach(index -> assertThat(responses.get(index))
                        .returns(categoryDTOs.get(index).id(), RetrieveCategoryResponse::id)
                        .returns(categoryDTOs.get(index).name(), RetrieveCategoryResponse::name)
                );
    }

    @Test
    void create_return_201() throws Exception {
        final var requestBody = new CreateCategoryRequest(randomShortString());
        final var newCategoryId = randomShortString();
        when(categoryService.create(anyString(), anyString())).thenReturn(newCategoryId);

        this.mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isCreated());

        then(categoryService).should().create(
                mockUser.userId(),
                requestBody.name()
        );
    }

    @Test
    void modify_return_204() throws Exception {
        final var requestBody = new ModifyCategoryRequest(randomShortString());
        final var modifyCategoryId = randomShortString();

        this.mockMvc.perform(patch("/api/categories/" + modifyCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());

        then(categoryService).should().modify(
                mockUser.userId(),
                modifyCategoryId,
                requestBody.name()
        );
    }

    @Test
    void modify_return_404() throws Exception {
        final var requestBody = new ModifyCategoryRequest(randomShortString());
        final var modifyCategoryId = randomShortString();

        doThrow(CategoryService.CategoryNotExisted.class)
                .when(categoryService).modify(anyString(), anyString(), anyString());

        this.mockMvc.perform(patch("/api/categories/" + modifyCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestBody))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNotFound());

        then(categoryService).should().modify(
                mockUser.userId(),
                modifyCategoryId,
                requestBody.name()
        );
    }

    @Test
    void delete_return_204() throws Exception {
        final var deleteCategoryId = randomShortString();

        this.mockMvc.perform(delete("/api/categories/" + deleteCategoryId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + randomShortString()))
                .andExpect(status().isNoContent());

        then(categoryService).should().delete(
                mockUser.userId(),
                deleteCategoryId
        );
    }

    private Supplier<RetrieveCategoryDto> randomCategoryDTO() {
        return () -> new RetrieveCategoryDto(
                randomShortString(),
                randomShortString()
        );
    }

}
