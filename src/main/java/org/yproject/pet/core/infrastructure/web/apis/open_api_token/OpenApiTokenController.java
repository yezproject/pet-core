package org.yproject.pet.core.infrastructure.web.apis.open_api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.core.application.open_api_token.OpenApiTokenService;
import org.yproject.pet.core.infrastructure.web.security.RequestUser;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tokens")
class OpenApiTokenController {
    private final OpenApiTokenService openApiTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    OpenApiTokenCreateResponse create(@RequestUser UserInfo user, @RequestBody OpenApiTokenCreateRequest request) {
        final var dto = openApiTokenService.create(user.getId(), user.getEmail(), request.name());
        return new OpenApiTokenCreateResponse(dto.id(), dto.token());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<OpenApiTokenRetrieveResponse> retrieve(@RequestUser UserInfo user) {
        return openApiTokenService.retrieveAllByUserId(user.getId()).stream()
                .map(dto -> new OpenApiTokenRetrieveResponse(dto.id(), dto.name()))
                .toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestUser UserInfo user, @RequestParam("id") String userTokenId) {
        openApiTokenService.delete(user.getId(), userTokenId);
    }

}
