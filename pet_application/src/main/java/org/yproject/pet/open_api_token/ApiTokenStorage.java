package org.yproject.pet.open_api_token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.Storage;
import org.yproject.pet.api_token.entities.ApiToken;
import org.yproject.pet.api_token.entities.ApiTokenBuilder;
import org.yproject.pet.open_api_token.driving.ApiTokenDao;
import org.yproject.pet.open_api_token.driving.ApiTokenDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Storage
class ApiTokenStorage {
    private final ApiTokenDao dao;

    Set<ApiToken> findByUserId(String userId) {
        return dao.findByUserId(userId)
                .stream().map(this::toDomain)
                .collect(Collectors.toSet());
    }

    String store(ApiToken apiToken) {
        return dao.store(fromDomain(apiToken));
    }

    void deleteById(String userId, String id) {
        dao.deleteById(userId, id);
    }

    ApiTokenDto fromDomain(ApiToken domain) {
        return ApiTokenDto.builder()
                .id(domain.getId().toString())
                .userId(domain.getUserId().toString())
                .name(domain.getName()).build();
    }

    ApiToken toDomain(ApiTokenDto dto) {
        return new ApiTokenBuilder(dto.getId())
                .userId(dto.getUserId())
                .name(dto.getName())
                .build();
    }
}
