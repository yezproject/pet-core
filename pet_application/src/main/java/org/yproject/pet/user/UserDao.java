package org.yproject.pet.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<SavedUserDto> findById(String id);

    Optional<SavedUserDto> findByEmail(String email);

    List<SavedUserDto> findALl();

    String store(SavedUserDto user);

    void deleteById(String id);
}
