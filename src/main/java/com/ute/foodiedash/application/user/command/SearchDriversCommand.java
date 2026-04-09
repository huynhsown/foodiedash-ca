package com.ute.foodiedash.application.user.command;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;

import java.time.Instant;

public record SearchDriversCommand(
        String keyword,
        UserStatus userStatus,
        DriverVerificationStatus driverVerificationStatus,
        VehicleType vehicleType,
        Instant createdFrom,
        Instant createdTo,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {}
