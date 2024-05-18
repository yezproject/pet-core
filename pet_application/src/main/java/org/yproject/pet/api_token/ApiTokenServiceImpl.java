package org.yproject.pet.api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.api_token.driven.ApiTokenIdWithNameDto;
import org.yproject.pet.api_token.driven.ApiTokenIdWithTokenDto;
import org.yproject.pet.api_token.driven.ApiTokenService;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.jwt.JwtService;

import java.util.List;

@Component
@RequiredArgsConstructor
class ApiTokenServiceImpl implements ApiTokenService {
    private final IdGenerator idGenerator;
    private final JwtService jwtService;
    private final ApiTokenStorage apiTokenStorage;

    @Override
    public ApiTokenIdWithTokenDto create(String userId, String email, String name) {
        final var apiTokenId = idGenerator.get();
        apiTokenStorage.store(new ApiTokenBuilder(apiTokenId)
                .userId(userId)
                .name(name)
                .build()
        );
        final var oneTimeToken = jwtService.generateToken(email, apiTokenId);
        return new ApiTokenIdWithTokenDto(apiTokenId, oneTimeToken);
    }

    @Override
    public List<ApiTokenIdWithNameDto> retrieveAllByUserId(String userId) {
        return apiTokenStorage.findByUserId(userId)
                .stream()
                .map(domain -> new ApiTokenIdWithNameDto(domain.getId(), domain.getName()))
                .toList();
    }

    @Override
    public void delete(String userId, String id) {
        apiTokenStorage.deleteById(userId, id);
    }
}
