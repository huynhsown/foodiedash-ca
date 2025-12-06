package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverProfile extends BaseEntity {

    private Long id;
    private Long userId;

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

    public static DriverProfile create(Long userId, VehicleType vehicleType) {
        if (userId == null) {
            throw new BadRequestException("USER_ID_REQUIRED");
        }

        DriverProfile profile = new DriverProfile();
        profile.userId = userId;
        profile.vehicleType = vehicleType != null ? vehicleType : VehicleType.MOTORBIKE;
        profile.isOnline = false;
        profile.verificationStatus = DriverVerificationStatus.PENDING;
        return profile;
    }

    public void updateIdentityInfo(
        String idCardNumber,
        String idCardFrontUrl,
        String idCardBackUrl
    ) {
        if (idCardNumber != null && !idCardNumber.isBlank()) {
            this.idCardNumber = idCardNumber;
        }
        if (idCardFrontUrl != null) {
            this.idCardFrontUrl = idCardFrontUrl;
        }
        if (idCardBackUrl != null) {
            this.idCardBackUrl = idCardBackUrl;
        }
    }

    public void updateLicenseInfo(String licenseNumber, String driverLicenseUrl) {
        if (licenseNumber != null && !licenseNumber.isBlank()) {
            this.licenseNumber = licenseNumber;
        }
        if (driverLicenseUrl != null) {
            this.driverLicenseUrl = driverLicenseUrl;
        }
    }

    public void updateVehicleInfo(VehicleType vehicleType, String vehiclePlate) {
        if (vehicleType != null) {
            this.vehicleType = vehicleType;
        }
        if (vehiclePlate != null) {
            this.vehiclePlate = vehiclePlate;
        }
    }

    public void updateBankInfo(String bankName, String bankAccount, String bankHolderName) {
        if (bankName != null) {
            this.bankName = bankName;
        }
        if (bankAccount != null) {
            this.bankAccount = bankAccount;
        }
        if (bankHolderName != null) {
            this.bankHolderName = bankHolderName;
        }
    }

    public void updateLocation(BigDecimal lat, BigDecimal lng) {
        this.currentLat = lat;
        this.currentLng = lng;
    }

    public boolean isApproved() {
        return this.verificationStatus == DriverVerificationStatus.APPROVED;
    }

    public void approve() {
        if (this.verificationStatus == DriverVerificationStatus.APPROVED) {
            throw new BadRequestException("DRIVER_ALREADY_APPROVED");
        }
        this.verificationStatus = DriverVerificationStatus.APPROVED;
    }

    public void reject() {
        if (this.verificationStatus == DriverVerificationStatus.REJECTED) {
            throw new BadRequestException("DRIVER_ALREADY_REJECTED");
        }
        this.verificationStatus = DriverVerificationStatus.REJECTED;
    }

    public void goOnline() {
        if (!isApproved()) {
            throw new BadRequestException("DRIVER_NOT_APPROVED");
        }
        this.isOnline = true;
    }

    public void goOffline() {
        this.isOnline = false;
    }
}

