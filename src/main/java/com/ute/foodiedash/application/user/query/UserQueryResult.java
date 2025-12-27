package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.model.User;

import java.time.Instant;

public record UserQueryResult(
        Long id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        UserStatus status,
        Instant createdAt
) {
    public static UserQueryResult from(User user) {
        return new UserQueryResult(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
