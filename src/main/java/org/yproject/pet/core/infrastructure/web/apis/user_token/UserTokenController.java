package org.yproject.pet.core.infrastructure.web.apis.user_token;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.core.application.user_token.UserTokenService;
import org.yproject.pet.core.infrastructure.web.security.RequestUser;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;

import java.util.List;

@RestController
@RequestMapping("tokens")
record UserTokenController(
        UserTokenService userTokenService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    UserTokenCreateResponse create(@RequestUser UserInfo user, @RequestBody @Valid UserTokenCreateRequest request) {
        final var dto = userTokenService.create(user.getId(), request.name());
        return new UserTokenCreateResponse(dto.id(), dto.token());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserTokenRetrieveResponse> retrieve(@RequestUser UserInfo user) {
        return userTokenService.retrieveAllByUserId(user.getId()).stream()
                .map(dto -> new UserTokenRetrieveResponse(dto.id(), dto.name()))
                .toList();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestUser UserInfo user, @RequestParam("id") String userTokenId) {
        userTokenService.delete(user.getId(), userTokenId);
    }

}
