package com.ute.foodiedash.interfaces.rest.driver.dto;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfileResponseDTO {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private Instant createdAt;
    private String idCardNumber;
    private String idCardFrontUrl;
    private String idCardBackUrl;
    private String licenseNumber;
    private VehicleType vehicleType;
    private String vehiclePlate;
    private String driverLicenseUrl;
    private String bankName;
    private String bankAccount;
    private String bankHolderName;
    private BigDecimal currentLat;
    private BigDecimal currentLng;
    private boolean isOnline;
    private DriverVerificationStatus verificationStatus;
}
