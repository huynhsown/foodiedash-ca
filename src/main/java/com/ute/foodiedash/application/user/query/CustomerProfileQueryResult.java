package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.user.enums.Gender;
import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.domain.user.model.User;

import java.time.Instant;

public record CustomerProfileQueryResult(
        Long id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        Instant createdAt,
        Instant dateOfBirth,
        Gender gender
) {
    public static CustomerProfileQueryResult from(User user) {
        CustomerProfile profile = user.getCustomerProfile();
        return new CustomerProfileQueryResult(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                profile != null ? profile.getDateOfBirth() : null,
                profile != null ? profile.getGender() : null
        );
    }
}

