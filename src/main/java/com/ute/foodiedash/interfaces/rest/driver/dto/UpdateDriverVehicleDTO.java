package com.ute.foodiedash.interfaces.rest.driver.dto;

import com.ute.foodiedash.domain.user.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverVehicleDTO {

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Vehicle plate is required")
    @Size(max = 20, message = "Vehicle plate must not exceed 20 characters")
    private String vehiclePlate;
}
