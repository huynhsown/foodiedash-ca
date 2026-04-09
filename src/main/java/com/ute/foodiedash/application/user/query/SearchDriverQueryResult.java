package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.model.UserRole;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record SearchDriverQueryResult(
        Long id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        UserStatus status,
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
        DriverVerificationStatus verificationStatus,
        List<RoleName> roles
) {
    public static SearchDriverQueryResult from(User user) {
        var profile = user.getDriverProfile();
        return new SearchDriverQueryResult(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getStatus(),
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
                profile != null ? profile.getDriverVerificationStatus() : null,
                user.getRoles().stream()
                        .map(UserRole::getRoleName)
                        .toList()
        );
    }
}
