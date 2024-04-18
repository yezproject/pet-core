package org.yproject.pet.repository.api_token;

import org.springframework.stereotype.Component;
import org.yproject.pet.open_api_token.OpenApiTokenStorage;
import org.yproject.pet.api_token.entities.ApiToken;
import org.yproject.pet.api_token.entities.ApiTokenBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@Component
record OpenApiTokenSpringDataStorage(
        ApiTokenRepository repository
) implements OpenApiTokenStorage {

    private static ApiTokenEntity toEntity(final ApiToken domain) {
        return new ApiTokenEntity(
                domain.getId().toString(),
                domain.getUserId().toString(),
                domain.getName()
        );
    }

    private static ApiToken fromEntity(final ApiTokenEntity entity) {
        return new ApiTokenBuilder(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName()).build();
    }

    @Override
    public Set<ApiToken> findByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(OpenApiTokenSpringDataStorage::fromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public String store(ApiToken apiToken) {
        return repository.save(toEntity(apiToken)).getId();
    }

    @Override
    public void deleteById(String userId, String id) {
        repository.findById(id).ifPresent(entity -> {
            if (entity.getUserId().equals(userId)) {
                repository.delete(entity);
            }
        });
    }
}
