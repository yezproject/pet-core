package org.yezproject.pet.repository.user;

import org.springframework.stereotype.Component;
import org.yezproject.pet.user.ApprovalStatus;
import org.yezproject.pet.user.Role;
import org.yezproject.pet.user.User;
import org.yezproject.pet.user.UserBuilder;
import org.yezproject.pet.user.driving.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
record UserRepositoryImpl(
        UserJpaRepository userJpaRepository
) implements UserRepository {
    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public String store(User user) {
        final var newUserEntity = userJpaRepository.save(toEntity(user));
        return newUserEntity.getId();
    }

    @Override
    public void deleteById(String id) {
        userJpaRepository.deleteById(id);
    }

    private UserEntity toEntity(final User dto) {
        return new UserEntity(
                dto.getId(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getFullName(),
                dto.getRole().name(),
                dto.getApprovalStatus().name(),
                dto.getCreateAt(),
                dto.getApprovedAt()
        );
    }

    private User toDomain(final UserEntity entity) {
        return new UserBuilder(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .fullName(entity.getFullName())
                .role(Role.valueOf(entity.getRole()))
                .approvalStatus(ApprovalStatus.valueOf(entity.getStatus()))
                .createAt(entity.getCreateAt())
                .approvedAt(entity.getApprovedAt())
                .build();
    }
}
