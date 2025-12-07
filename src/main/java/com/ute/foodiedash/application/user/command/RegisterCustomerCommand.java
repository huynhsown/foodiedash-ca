package com.ute.foodiedash.application.user.command;

import com.ute.foodiedash.domain.user.enums.Gender;

import java.time.Instant;

public record RegisterCustomerCommand(
        String email,
        String phone,
        String password,
        String fullName,
        Instant dateOfBirth,
        Gender gender
) {}
