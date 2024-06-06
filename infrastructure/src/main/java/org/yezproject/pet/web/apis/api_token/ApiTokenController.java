package org.yezproject.pet.web.apis.api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.api_token.driven.ApiTokenService;
import org.yezproject.pet.web.security.RequestUser;
import org.yezproject.pet.web.security.UserInfo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
class ApiTokenController {
    private final ApiTokenService apiTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ApiTokenCreateResponse create(@RequestUser UserInfo user, @RequestBody ApiTokenCreateRequest request) {
        final var dto = apiTokenService.create(user.id(), user.email(), request.name());
        return new ApiTokenCreateResponse(dto.id(), dto.token());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ApiTokenRetrieveResponse> retrieve(@RequestUser UserInfo user) {
        return apiTokenService.retrieveAllByUserId(user.id()).stream()
                .map(dto -> new ApiTokenRetrieveResponse(dto.id(), dto.name()))
                .toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestUser UserInfo user, @RequestParam("id") String userTokenId) {
        apiTokenService.delete(user.id(), userTokenId);
    }

}
