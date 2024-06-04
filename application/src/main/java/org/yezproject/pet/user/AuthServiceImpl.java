package org.yezproject.pet.user;

import org.springframework.stereotype.Component;
import org.yezproject.pet.user.driven.AuthInfo;
import org.yezproject.pet.user.driven.AuthService;
import org.yezproject.pet.user.driving.UserRepository;

@Component
record AuthServiceImpl(
        UserRepository userRepository
) implements AuthService {

    @Override
    public AuthInfo loadUserByEmail(String email) throws UserNotFoundException {
        final var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User email %s not found".formatted(email));
        else {
            final var user = optionalUser.get();
            return new AuthInfo(user.getId(), user.getEmail());
        }
    }

    @Override
    public AuthInfo loadUserById(String userId) throws UserNotFoundException {
        final var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User Id %s not found".formatted(userId));
        else {
            final var user = optionalUser.get();
            return new AuthInfo(user.getId(), user.getEmail());
        }
    }
}
