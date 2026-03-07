package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.MerchantVerificationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantProfile extends BaseEntity {

    private Long id;
    private Long userId;

    private String businessName;
    private String businessLicense;
    private String taxCode;

    private String bankName;
    private String bankAccount;
    private String bankHolderName;

    private String contactEmail;
    private String contactPhone;

    private MerchantVerificationStatus verificationStatus;

    public static MerchantProfile create(
            Long userId,
            String businessName,
            String contactEmail,
            String contactPhone
    ) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        if (businessName == null || businessName.isBlank()) {
            throw new BadRequestException("Business name is required");
        }

        MerchantProfile profile = new MerchantProfile();
        profile.userId = userId;
        profile.businessName = businessName;
        profile.contactEmail = contactEmail;
        profile.contactPhone = contactPhone;
        profile.verificationStatus = MerchantVerificationStatus.PENDING;

        return profile;
    }

    public void updateBusinessInfo(
            String businessName,
            String businessLicense,
            String taxCode
    ) {

        if (businessName != null && !businessName.isBlank()) {
            this.businessName = businessName;
        }

        if (businessLicense != null) {
            this.businessLicense = businessLicense;
        }

        if (taxCode != null) {
            this.taxCode = taxCode;
        }
    }

    public void updateBankInfo(
            String bankName,
            String bankAccount,
            String bankHolderName
    ) {

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

    public void updateContactInfo(String contactEmail, String contactPhone) {

        if (contactEmail != null) {
            this.contactEmail = contactEmail;
        }

        if (contactPhone != null) {
            this.contactPhone = contactPhone;
        }
    }

    public void approve() {

        if (this.verificationStatus == MerchantVerificationStatus.APPROVED) {
            throw new BadRequestException("Merchant is already approved");
        }

        this.verificationStatus = MerchantVerificationStatus.APPROVED;
    }

    public void reject() {

        if (this.verificationStatus == MerchantVerificationStatus.REJECTED) {
            throw new BadRequestException("Merchant is already rejected");
        }

        this.verificationStatus = MerchantVerificationStatus.REJECTED;
    }

}