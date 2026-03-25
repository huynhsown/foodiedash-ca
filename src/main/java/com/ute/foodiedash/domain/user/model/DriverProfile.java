package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

@Getter
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
    private DriverVerificationStatus driverVerificationStatus;

    private DriverProfile() {}

    public static DriverProfile create(Long userId, VehicleType vehicleType) {

        DriverProfile profile = new DriverProfile();
        profile.userId = userId;
        profile.vehicleType = vehicleType != null ? vehicleType : VehicleType.MOTORBIKE;
        profile.isOnline = false;
        profile.driverVerificationStatus = DriverVerificationStatus.DRAFT;
        return profile;
    }

    public static DriverProfile reconstruct(
            Long id,
            Long userId,
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
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        DriverProfile profile = new DriverProfile();
        profile.id = id;
        profile.userId = userId;
        profile.idCardNumber = idCardNumber;
        profile.idCardFrontUrl = idCardFrontUrl;
        profile.idCardBackUrl = idCardBackUrl;
        profile.licenseNumber = licenseNumber;
        profile.vehicleType = vehicleType;
        profile.vehiclePlate = vehiclePlate;
        profile.driverLicenseUrl = driverLicenseUrl;
        profile.bankName = bankName;
        profile.bankAccount = bankAccount;
        profile.bankHolderName = bankHolderName;
        profile.currentLat = currentLat;
        profile.currentLng = currentLng;
        profile.isOnline = isOnline;
        profile.driverVerificationStatus = verificationStatus;
        profile.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
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
        return this.driverVerificationStatus == DriverVerificationStatus.APPROVED;
    }

    public void submit() {
        ensureCanSubmit();
        if (!hasMinimumVerificationData()) {
            throw new BadRequestException(
                    "You must provide either identity documents or driving license information"
            );
        }
        this.driverVerificationStatus = DriverVerificationStatus.PENDING;
    }

    public void approve() {
        ensurePending();
        this.driverVerificationStatus = DriverVerificationStatus.APPROVED;
    }

    public void reject() {
        ensurePending();
        this.driverVerificationStatus = DriverVerificationStatus.REJECTED;
    }

    public void goOnline() {
        if (!isApproved()) {
            throw new BadRequestException("Driver must be approved before going online");
        }
        this.isOnline = true;
    }

    public void goOffline() {
        this.isOnline = false;
    }

    private void ensureCanSubmit() {
        if (driverVerificationStatus == DriverVerificationStatus.PENDING) {
            throw new BadRequestException("Verification already submitted");
        }
    }

    private void ensurePending() {
        if (driverVerificationStatus != DriverVerificationStatus.PENDING) {
            throw new BadRequestException("Driver must be in PENDING state");
        }
    }

    private boolean hasMinimumVerificationData() {

        boolean hasIdentity =
                isNotBlank(idCardNumber)
                        && isNotBlank(idCardFrontUrl)
                        && isNotBlank(idCardBackUrl);

        boolean hasLicense =
                isNotBlank(licenseNumber)
                        && isNotBlank(driverLicenseUrl)
                        && isNotBlank(vehiclePlate);

        return hasIdentity || hasLicense;

    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}

