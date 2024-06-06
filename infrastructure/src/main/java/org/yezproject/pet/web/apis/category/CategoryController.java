package org.yezproject.pet.web.apis.category;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.category.driven.CategoryService;
import org.yezproject.pet.web.security.RequestUser;
import org.yezproject.pet.web.security.UserInfo;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category", description = "Category management")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateCategoryResponse create(
            @RequestBody CreateCategoryRequest req,
            @RequestUser UserInfo user
    ) {
        final var createdId = categoryService.create(user.id(), req.name());
        return new CreateCategoryResponse(createdId);
    }

    @GetMapping
    List<RetrieveCategoryResponse> retrieveAll(
            @RequestUser UserInfo user
    ) {
        return categoryService.retrieveAll(user.id())
                .stream().map(dto -> new RetrieveCategoryResponse(dto.id(), dto.name()))
                .toList();
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void modifyName(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ModifyCategoryRequest request,
            @RequestUser UserInfo user
    ) {
        categoryService.modify(user.id(), categoryId, request.name());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @PathVariable("categoryId") String categoryId,
            @RequestUser UserInfo user
    ) {
        categoryService.delete(user.id(), categoryId);
    }

    @ExceptionHandler(CategoryService.CategoryNotExisted.class)
    ResponseEntity<Void> categoryNotExist() {
        return ResponseEntity.notFound().build();
    }
}
