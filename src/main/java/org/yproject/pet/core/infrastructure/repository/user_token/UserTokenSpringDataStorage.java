package org.yproject.pet.core.infrastructure.repository.user_token;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user_token.UserTokenStorage;
import org.yproject.pet.core.domain.user_token.UserToken;

import java.util.Set;
import java.util.stream.Collectors;

@Component
record UserTokenSpringDataStorage(
        UserTokenRepository repository
) implements UserTokenStorage {

    private static UserTokenEntity toEntity(final UserToken domain) {
        return new UserTokenEntity(
                domain.id(),
                domain.userId(),
                domain.name(),
                domain.secret()
        );
    }

    private static UserToken fromEntity(final UserTokenEntity entity) {
        return new UserToken(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getSecret()
        );
    }

    @Override
    public Set<UserToken> findByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(UserTokenSpringDataStorage::fromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public String store(UserToken userToken) {
        return repository.save(toEntity(userToken)).getId();
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
