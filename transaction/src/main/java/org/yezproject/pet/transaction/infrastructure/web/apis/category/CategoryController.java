package org.yezproject.pet.transaction.infrastructure.web.apis.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.transaction.application.category.driven.CategoryService;
import org.yezproject.pet.transaction.infrastructure.web.security.RequestUser;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateCategoryResponse create(
            @RequestBody CreateCategoryRequest req,
            @RequestUser PetUserDetails user
    ) {
        final var createdId = categoryService.create(user.id(), req.name());
        return new CreateCategoryResponse(createdId);
    }

    @GetMapping
    List<RetrieveCategoryResponse> retrieveAll(
            @RequestUser PetUserDetails user
    ) {
        return categoryService.retrieveAll(user.id())
                .stream().map(dto -> new RetrieveCategoryResponse(dto.id(), dto.name()))
                .toList();
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void modifyName(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ModifyCategoryRequest request,
            @RequestUser PetUserDetails user
    ) {
        categoryService.modify(user.id(), categoryId, request.name());
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @PathVariable("categoryId") String categoryId,
            @RequestUser PetUserDetails user
    ) {
        categoryService.delete(user.id(), categoryId);
    }

    @ExceptionHandler(CategoryService.CategoryNotExisted.class)
    ResponseEntity<Void> categoryNotExist() {
        return ResponseEntity.notFound().build();
    }
}
