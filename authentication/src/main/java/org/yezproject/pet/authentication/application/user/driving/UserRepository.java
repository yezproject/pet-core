package org.yezproject.pet.authentication.application.user.driving;

import org.yezproject.pet.authentication.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    String store(User user);

    void deleteById(String id);
}
