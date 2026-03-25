package com.ute.foodiedash.application.user.command;

public record UpdateDriverLicenseCommand(
        String licenseNumber,
        String driverLicenseUrl
) {
}
