package org.yezproject.pet.authentication.application.user;

import org.yezproject.pet.authentication.application.user.driven.GeneralUserDto;
import org.yezproject.pet.authentication.application.user.driven.UserQuery;
import org.yezproject.pet.authentication.application.user.driving.UserRepository;
import org.yezproject.pet.domain_common.UseCase;

@UseCase
record UserQueryImpl(
        UserRepository userRepository
) implements UserQuery {

    @Override
    public GeneralUserDto getByEmail(String email) throws UserNotFoundException {
        final var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User email %s not found".formatted(email));
        else {
            final var user = optionalUser.get();
            return new GeneralUserDto(user.getId(), user.getEmail(), user.getFullName());
        }
    }

    @Override
    public GeneralUserDto getById(String userId) throws UserNotFoundException {
        final var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User Id %s not found".formatted(userId));
        else {
            final var user = optionalUser.get();
            return new GeneralUserDto(user.getId(), user.getEmail(), user.getFullName());
        }
    }
}
