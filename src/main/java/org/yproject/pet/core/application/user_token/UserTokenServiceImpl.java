package org.yproject.pet.core.application.user_token;

import org.springframework.stereotype.Service;
import org.yproject.pet.core.domain.user_token.UserToken;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;
import org.yproject.pet.core.infrastructure.generator.key.SecretHashGenerator;

import java.util.List;

@Service
record UserTokenServiceImpl(
        IdGenerator idGenerator,
        SecretHashGenerator secretHashGenerator,
        UserTokenStorage userTokenStorage
) implements UserTokenService {

    public static final String DUMMYTOKEN = "DUMMYTOKEN";

    @Override
    public UserTokenIdWithTokenDto create(String userId, String name) {
        final var id = idGenerator.get();
        final var secret = secretHashGenerator.get(userId.concat(id));
        userTokenStorage.store(new UserToken(
                id,
                userId,
                name,
                secret
        ));
        /* TODO: require update return JWT token with user data to integrate with current JWT Auth Based */
        return new UserTokenIdWithTokenDto(id, DUMMYTOKEN);
    }

    @Override
    public List<UserTokenIdWithNameDto> retrieveAllByUserId(String userId) {
        return userTokenStorage.findByUserId(userId)
                .stream()
                .map(domain -> new UserTokenIdWithNameDto(domain.id(), domain.name()))
                .toList();
    }

    @Override
    public void delete(String userId, String id) {
        userTokenStorage.deleteById(userId, id);
    }
}
