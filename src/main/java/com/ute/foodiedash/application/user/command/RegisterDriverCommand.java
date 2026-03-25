package com.ute.foodiedash.application.user.command;

import com.ute.foodiedash.domain.user.enums.VehicleType;

public record RegisterDriverCommand(
        String email,
        String phone,
        String password,
        String fullName,
        VehicleType vehicleType
) {}
