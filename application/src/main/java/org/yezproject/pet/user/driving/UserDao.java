package org.yezproject.pet.user.driving;

import org.yezproject.pet.Dao;

import java.util.List;
import java.util.Optional;

@Dao
public interface UserDao {
    Optional<SavedUserDto> findById(String id);

    Optional<SavedUserDto> findByEmail(String email);

    List<SavedUserDto> findALl();

    String store(SavedUserDto user);

    void deleteById(String id);
}
