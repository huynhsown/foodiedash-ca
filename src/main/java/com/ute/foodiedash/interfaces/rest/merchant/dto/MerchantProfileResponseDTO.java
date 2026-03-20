package com.ute.foodiedash.interfaces.rest.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProfileResponseDTO {
    private AccountDTO account;
    private BusinessDTO business;
    private List<MerchantRestaurantDTO> restaurants;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountDTO {
        private Long id;
        private String email;
        private String phone;
        private String fullName;
        private String avatarUrl;
        private String status;
        private List<String> roleNames;
        private Instant createdAt;
        private Instant updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessDTO {
        private String businessName;
        private String businessLicense;
        private String taxCode;
        private String bankName;
        private String bankAccount;
        private String bankHolderName;
        private String contactEmail;
        private String contactPhone;
        private String verificationStatus;
        private Instant createdAt;
        private Instant updatedAt;
    }
}
