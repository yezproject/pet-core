package org.yproject.pet.repository.api_token;

import org.springframework.stereotype.Component;
import org.yproject.pet.api_token.driving.ApiTokenDao;
import org.yproject.pet.api_token.driving.ApiTokenDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
record ApiTokenDaoImpl(
        ApiTokenRepository repository
) implements ApiTokenDao {

    @Override
    public Set<ApiTokenDto> findByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public String store(ApiTokenDto apiToken) {
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

    private ApiTokenEntity toEntity(final ApiTokenDto domain) {
        return ApiTokenEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .name(domain.getName())
                .build();
    }

    private ApiTokenDto fromEntity(final ApiTokenEntity entity) {
        return ApiTokenDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName()).build();
    }

}
