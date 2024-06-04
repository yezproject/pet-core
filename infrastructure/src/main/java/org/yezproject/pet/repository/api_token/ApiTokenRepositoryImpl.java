package org.yezproject.pet.repository.api_token;

import org.springframework.stereotype.Component;
import org.yezproject.pet.api_token.ApiToken;
import org.yezproject.pet.api_token.ApiTokenBuilder;
import org.yezproject.pet.api_token.driving.ApiTokenRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
record ApiTokenRepositoryImpl(
        ApiTokenJpaRepository repository
) implements ApiTokenRepository {

    @Override
    public Set<ApiToken> findByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::toDomain)
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

    ApiTokenEntity toEntity(ApiToken domain) {
        return ApiTokenEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName()).build();
    }

    ApiToken toDomain(ApiTokenEntity entity) {
        return new ApiTokenBuilder(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .build();
    }

}
