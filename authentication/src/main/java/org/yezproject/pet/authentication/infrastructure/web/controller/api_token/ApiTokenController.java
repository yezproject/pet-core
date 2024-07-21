package org.yezproject.pet.authentication.infrastructure.web.controller.api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.authentication.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.RequestUser;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
class ApiTokenController {
    private final ApiTokenService apiTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ApiTokenCreateResponse create(@RequestUser PetUserDetails user, @RequestBody ApiTokenCreateRequest request) {
        final var dto = apiTokenService.create(user.id(), user.email(), request.name());
        return new ApiTokenCreateResponse(dto.id(), dto.token());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ApiTokenRetrieveResponse> retrieve(@RequestUser PetUserDetails user) {
        return apiTokenService.retrieveAllByUserId(user.id()).stream()
                .map(dto -> new ApiTokenRetrieveResponse(dto.id(), dto.name()))
                .toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestUser PetUserDetails user, @RequestParam("id") String userTokenId) {
        apiTokenService.delete(user.id(), userTokenId);
    }

}
