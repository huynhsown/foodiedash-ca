package com.ute.foodiedash.application.user.command;

import com.ute.foodiedash.domain.user.enums.VehicleType;

public record UpdateDriverVehicleCommand(
        VehicleType vehicleType,
        String vehiclePlate
) {
}
