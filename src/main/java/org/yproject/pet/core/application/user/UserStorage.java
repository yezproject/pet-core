package org.yproject.pet.core.application.user;

import org.yproject.pet.core.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findALl();

    String store(User user);

    void deleteById(String id);
}
