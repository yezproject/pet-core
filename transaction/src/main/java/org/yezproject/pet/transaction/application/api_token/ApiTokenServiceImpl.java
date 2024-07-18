package org.yezproject.pet.transaction.application.api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.yezproject.pet.transaction.application.api_token.driven.ApiTokenIdWithNameDto;
import org.yezproject.pet.transaction.application.api_token.driven.ApiTokenIdWithTokenDto;
import org.yezproject.pet.transaction.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.transaction.application.api_token.driven.UserIdDto;
import org.yezproject.pet.transaction.application.api_token.driving.ApiTokenRepository;
import org.yezproject.pet.transaction.application.id.IdGenerator;
import org.yezproject.pet.transaction.domain.api_token.ApiTokenBuilder;
import org.yezproject.pet.transaction.domain.api_token.ApiToken;

import java.util.List;

@Component
@RequiredArgsConstructor
class ApiTokenServiceImpl implements ApiTokenService {
    private final IdGenerator idGenerator;
    private final ApiTokenGeneratorService apiTokenGeneratorService;
    private final ApiTokenRepository apiTokenRepository;

    @Override
    public ApiTokenIdWithTokenDto create(String userId, String email, String name) {
        final var apiTokenId = idGenerator.get();
        final var oneTimeToken = apiTokenGeneratorService.generate(userId, apiTokenId);
        apiTokenRepository.store(new ApiTokenBuilder(apiTokenId)
                .userId(userId)
                .name(name)
                .token(oneTimeToken)
                .build()
        );
        return new ApiTokenIdWithTokenDto(apiTokenId, oneTimeToken);
    }

    @Override
    public List<ApiTokenIdWithNameDto> retrieveAllByUserId(String userId) {
        return apiTokenRepository.findByUserId(userId)
                .stream()
                .map(domain -> new ApiTokenIdWithNameDto(domain.getId(), domain.getName()))
                .toList();
    }

    @Override
    public void delete(String userId, String id) {
        apiTokenRepository.deleteById(userId, id);
    }

    @Override
    @Cacheable(value = "token")
    public UserIdDto verify(String token) throws InvalidTokenException {
        return apiTokenRepository.findByToken(token)
                .map(ApiToken::getUserId)
                .map(UserIdDto::new)
                .orElseThrow(InvalidTokenException::new);
    }
}
