package org.yproject.pet.core.infrastructure.repository.open_api_token;

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
@Entity(name = "open_api_tokens")
public class OpenApiToken {
    @Id
    private String id;

    @Column(nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

}
