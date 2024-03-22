package org.yproject.pet.core.infrastructure.repository.open_api_token;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.open_api_token.OpenApiTokenStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Component
record OpenApiTokenSpringDataStorage(
        OpenApiTokenRepository repository
) implements OpenApiTokenStorage {

    private static OpenApiToken toEntity(final org.yproject.pet.core.domain.open_api_token.OpenApiToken domain) {
        return new OpenApiToken(
                domain.id(),
                domain.userId(),
                domain.name()
        );
    }

    private static org.yproject.pet.core.domain.open_api_token.OpenApiToken fromEntity(final OpenApiToken entity) {
        return new org.yproject.pet.core.domain.open_api_token.OpenApiToken(
                entity.getId(),
                entity.getUserId(),
                entity.getName()
        );
    }

    @Override
    public Set<org.yproject.pet.core.domain.open_api_token.OpenApiToken> findByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(OpenApiTokenSpringDataStorage::fromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public String store(org.yproject.pet.core.domain.open_api_token.OpenApiToken openApiToken) {
        return repository.save(toEntity(openApiToken)).getId();
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
