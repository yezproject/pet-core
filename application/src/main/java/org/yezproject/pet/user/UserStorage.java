package org.yezproject.pet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yezproject.pet.Storage;
import org.yezproject.pet.user.driving.SavedUserDto;
import org.yezproject.pet.user.driving.UserDao;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Storage
class UserStorage {
    private final UserDao userDAO;

    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email).map(this::toDomain);
    }

    public String store(User user) {
        return userDAO.store(toDto(user));
    }

    private User toDomain(SavedUserDto dto) {
        return new UserBuilder(dto.getId())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(dto.getPassword())
                .role(Role.valueOf(dto.getRole()))
                .approvalStatus(ApprovalStatus.valueOf(dto.getStatus()))
                .createAt(dto.getCreateAt())
                .approvedAt(dto.getApprovedAt())
                .build();
    }

    private SavedUserDto toDto(final User domain) {
        return new SavedUserDto(
                domain.getId(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getFullName(),
                domain.getRole().name(),
                domain.getApprovalStatus().name(),
                domain.getCreateAt(),
                domain.getApprovedAt()
        );
    }
}
