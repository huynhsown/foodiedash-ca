package com.ute.foodiedash.application.user.command;

import com.ute.foodiedash.domain.user.enums.Gender;

import java.time.Instant;

public record UpdateCustomerProfileCommand(
        String fullName,
        String phone,
        String avatarUrl,
        Instant dateOfBirth,
        Gender gender
) {
}

