package org.yproject.pet.core.application.user_token;

import org.springframework.stereotype.Service;
import org.yproject.pet.core.domain.user_token.UserToken;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;
import org.yproject.pet.core.infrastructure.web.jwt.JwtService;

import java.util.List;

@Service
record UserTokenServiceImpl(
        IdGenerator idGenerator,
        JwtService jwtService,
        UserTokenStorage userTokenStorage
) implements UserTokenService {

    @Override
    public UserTokenIdWithTokenDto create(String userId, String email, String name) {
        final var id = idGenerator.get();
        userTokenStorage.store(new UserToken(
                id,
                userId,
                name
        ));
        final var oneTimeToken = jwtService.generateToken(email, id);
        return new UserTokenIdWithTokenDto(id, oneTimeToken);
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
