package org.yproject.pet.core.infrastructure.repository.user;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.domain.user.entities.User;
import org.yproject.pet.core.domain.user.entities.UserBuilder;
import org.yproject.pet.core.domain.user.enums.ApprovalStatus;
import org.yproject.pet.core.domain.user.enums.Role;

import java.util.List;
import java.util.Optional;

@Component
record UserSpringDataStorage(
        UserRepository repository
) implements UserStorage {
    private static UserEntity toEntity(final User domain) {
        return new UserEntity(
                domain.getId().value(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getFullName(),
                domain.getRole().name(),
                domain.getApprovalStatus().name(),
                domain.getCreateAt(),
                domain.getApprovedAt()
        );
    }

    private static User fromEntity(final UserEntity entity) {
        return new UserBuilder(entity.getId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .password(entity.getPassword())
                .role(Role.valueOf(entity.getRole()))
                .approvalStatus(ApprovalStatus.valueOf(entity.getStatus()))
                .createAt(entity.getCreateAt())
                .approvedAt(entity.getApprovedAt())
                .build();
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
