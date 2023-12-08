package y.project.core.application.user;

import y.project.core.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findById(String id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findALl();

    String store(User user);

    void deleteById(String id);
}
