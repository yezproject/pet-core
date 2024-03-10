package org.yproject.pet.core.infrastructure.repository.user;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.domain.user.ApprovalStatus;
import org.yproject.pet.core.domain.user.Role;
import org.yproject.pet.core.domain.user.User;

import java.util.List;
import java.util.Optional;

@Component
record UserSpringDataStorage(
        UserRepository repository
) implements UserStorage {
    private static UserEntity toEntity(final User domain) {
        return new UserEntity(
                domain.id(),
                domain.email(),
                domain.password(),
                domain.fullName(),
                domain.role().name(),
                domain.approvalStatus().name(),
                domain.createAt(),
                domain.approvedAt()
        );
    }

    private static User fromEntity(final UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getFullName(),
                entity.getPassword(),
                Role.valueOf(entity.getRole()),
                ApprovalStatus.valueOf(entity.getStatus()),
                entity.getCreateAt(),
                entity.getApprovedAt()
        );
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id)
                .map(UserSpringDataStorage::fromEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserSpringDataStorage::fromEntity);
    }

    @Override
    public List<User> findALl() {
        return repository.findAll()
                .stream()
                .map(UserSpringDataStorage::fromEntity)
                .toList();
    }

    @Override
    public String store(User user) {
        final var newUserEntity = repository.save(toEntity(user));
        return newUserEntity.getId();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
