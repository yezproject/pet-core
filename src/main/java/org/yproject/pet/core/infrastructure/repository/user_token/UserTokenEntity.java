package org.yproject.pet.core.infrastructure.repository.user_token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "user_tokens")
class UserTokenEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

}
