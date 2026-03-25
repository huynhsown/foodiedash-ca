package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.User;

import java.math.BigDecimal;
import java.time.Instant;

public record DriverProfileQueryResult(
        Long id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        Instant createdAt,
        String idCardNumber,
        String idCardFrontUrl,
        String idCardBackUrl,
        String licenseNumber,
        VehicleType vehicleType,
        String vehiclePlate,
        String driverLicenseUrl,
        String bankName,
        String bankAccount,
        String bankHolderName,
        BigDecimal currentLat,
        BigDecimal currentLng,
        boolean isOnline,
        DriverVerificationStatus verificationStatus
) {
    public static DriverProfileQueryResult from(User user) {
        var profile = user.getDriverProfile();
        return new DriverProfileQueryResult(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                profile != null ? profile.getIdCardNumber() : null,
                profile != null ? profile.getIdCardFrontUrl() : null,
                profile != null ? profile.getIdCardBackUrl() : null,
                profile != null ? profile.getLicenseNumber() : null,
                profile != null ? profile.getVehicleType() : null,
                profile != null ? profile.getVehiclePlate() : null,
                profile != null ? profile.getDriverLicenseUrl() : null,
                profile != null ? profile.getBankName() : null,
                profile != null ? profile.getBankAccount() : null,
                profile != null ? profile.getBankHolderName() : null,
                profile != null ? profile.getCurrentLat() : null,
                profile != null ? profile.getCurrentLng() : null,
                profile != null && profile.isOnline(),
                profile != null ? profile.getDriverVerificationStatus() : null
        );
    }
}
