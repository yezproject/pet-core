package org.yproject.pet.repository.user;

import org.springframework.stereotype.Component;
import org.yproject.pet.user.SavedUserDto;
import org.yproject.pet.user.UserDao;

import java.util.List;
import java.util.Optional;

@Component
record UserDaoImpl(
        UserRepository repository
) implements UserDao {
    @Override
    public Optional<SavedUserDto> findById(String id) {
        return repository.findById(id)
                .map(this::fromEntity);
    }

    @Override
    public Optional<SavedUserDto> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::fromEntity);
    }

    @Override
    public List<SavedUserDto> findALl() {
        return repository.findAll()
                .stream()
                .map(this::fromEntity)
                .toList();
    }

    @Override
    public String store(SavedUserDto user) {
        final var newUserEntity = repository.save(toEntity(user));
        return newUserEntity.getId();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private UserEntity toEntity(final SavedUserDto dto) {
        return new UserEntity(
                dto.getId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getFullName(),
                dto.getRole(),
                dto.getStatus(),
                dto.getCreateAt(),
                dto.getApprovedAt()
        );
    }

    private SavedUserDto fromEntity(final UserEntity entity) {
        return new SavedUserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getFullName(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreateAt(),
                entity.getApprovedAt()
        );
    }
}
