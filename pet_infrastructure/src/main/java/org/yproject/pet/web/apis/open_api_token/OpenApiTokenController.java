package org.yproject.pet.web.apis.open_api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.open_api_token.driven.OpenApiTokenService;
import org.yproject.pet.web.security.RequestUser;
import org.yproject.pet.web.security.UserInfo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tokens")
class OpenApiTokenController {
    private final OpenApiTokenService openApiTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    OpenApiTokenCreateResponse create(@RequestUser UserInfo user, @RequestBody OpenApiTokenCreateRequest request) {
        final var dto = openApiTokenService.create(user.id(), user.email(), request.name());
        return new OpenApiTokenCreateResponse(dto.id(), dto.token());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<OpenApiTokenRetrieveResponse> retrieve(@RequestUser UserInfo user) {
        return openApiTokenService.retrieveAllByUserId(user.id()).stream()
                .map(dto -> new OpenApiTokenRetrieveResponse(dto.id(), dto.name()))
                .toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestUser UserInfo user, @RequestParam("id") String userTokenId) {
        openApiTokenService.delete(user.id(), userTokenId);
    }

}
