package com.ute.foodiedash.interfaces.rest.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverLicenseDTO {

    @NotBlank(message = "License number is required")
    @Size(max = 50, message = "License number must not exceed 50 characters")
    private String licenseNumber;

    @NotBlank(message = "Driver license image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String driverLicenseUrl;
}
