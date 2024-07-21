package org.yezproject.pet.authentication.application.api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.yezproject.pet.authentication.application.api_token.driven.ApiTokenIdWithNameDto;
import org.yezproject.pet.authentication.application.api_token.driven.ApiTokenIdWithTokenDto;
import org.yezproject.pet.authentication.application.api_token.driven.ApiTokenService;
import org.yezproject.pet.authentication.application.api_token.driven.UserDto;
import org.yezproject.pet.authentication.application.api_token.driving.ApiTokenRepository;
import org.yezproject.pet.authentication.application.id.IdGenerator;
import org.yezproject.pet.authentication.application.user.driven.UserQuery;
import org.yezproject.pet.authentication.domain.api_token.ApiTokenBuilder;
import org.yezproject.pet.domain_common.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
class ApiTokenServiceImpl implements ApiTokenService {
    private final IdGenerator idGenerator;
    private final ApiTokenGeneratorService apiTokenGeneratorService;
    private final ApiTokenRepository apiTokenRepository;
    private final UserQuery userQuery;

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
    public UserDto verify(String token) throws InvalidTokenException {
        return apiTokenRepository.findByToken(token)
                .map(apiToken -> {
                    try {
                        var user = userQuery.getById(apiToken.getUserId());
                        return new UserDto(user.userId(), user.email());
                    } catch (UserQuery.UserNotFoundException e) {
                        return null;
                    }
                })
                .orElseThrow(InvalidTokenException::new);
    }
}
