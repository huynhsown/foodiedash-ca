package com.ute.foodiedash.interfaces.rest.merchant.mapper;

import com.ute.foodiedash.application.user.query.MerchantProfileQueryResult;
import com.ute.foodiedash.interfaces.rest.merchant.dto.BusinessHourDTO;
import com.ute.foodiedash.interfaces.rest.merchant.dto.MerchantProfileResponseDTO;
import com.ute.foodiedash.interfaces.rest.merchant.dto.MerchantRestaurantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MerchantProfileDtoMapper {

    MerchantProfileResponseDTO toResponseDto(MerchantProfileQueryResult result);

    default MerchantProfileResponseDTO toResponse(MerchantProfileQueryResult result) {
        if (result == null) {
            return null;
        }

        MerchantProfileResponseDTO dto = new MerchantProfileResponseDTO();

        MerchantProfileResponseDTO.AccountDTO account = new MerchantProfileResponseDTO.AccountDTO();
        account.setId(result.id());
        account.setEmail(result.email());
        account.setPhone(result.phone());
        account.setFullName(result.fullName());
        account.setAvatarUrl(result.avatarUrl());
        account.setStatus(result.status() != null ? result.status().name() : null);
        account.setRoleNames(result.roleNames() != null
                ? result.roleNames().stream().map(Enum::name).collect(Collectors.toList())
                : Collections.emptyList());
        account.setCreatedAt(result.accountCreatedAt());
        account.setUpdatedAt(result.accountUpdatedAt());
        dto.setAccount(account);

        MerchantProfileResponseDTO.BusinessDTO business = new MerchantProfileResponseDTO.BusinessDTO();
        business.setBusinessName(result.businessName());
        business.setBusinessLicense(result.businessLicense());
        business.setTaxCode(result.taxCode());
        business.setBankName(result.bankName());
        business.setBankAccount(result.bankAccount());
        business.setBankHolderName(result.bankHolderName());
        business.setContactEmail(result.contactEmail());
        business.setContactPhone(result.contactPhone());
        business.setVerificationStatus(result.verificationStatus() != null ? result.verificationStatus().name() : null);
        business.setCreatedAt(result.businessCreatedAt());
        business.setUpdatedAt(result.businessUpdatedAt());
        dto.setBusiness(business);

        dto.setRestaurants(toRestaurantDtos(result.restaurants()));

        return dto;
    }

    default List<MerchantRestaurantDTO> toRestaurantDtos(
            List<MerchantProfileQueryResult.RestaurantSummary> summaries) {
        if (summaries == null) {
            return Collections.emptyList();
        }
        return summaries.stream()
                .map(this::toRestaurantDto)
                .collect(Collectors.toList());
    }

    default MerchantRestaurantDTO toRestaurantDto(MerchantProfileQueryResult.RestaurantSummary summary) {
        if (summary == null) {
            return null;
        }
        MerchantRestaurantDTO dto = new MerchantRestaurantDTO();
        dto.setId(summary.id());
        dto.setName(summary.name());
        dto.setAddress(summary.address());
        dto.setPhone(summary.phone());
        dto.setStatus(summary.status());
        dto.setBusinessHours(toBusinessHourDtos(summary.businessHours()));
        return dto;
    }

    default List<BusinessHourDTO> toBusinessHourDtos(
            List<MerchantProfileQueryResult.BusinessHourSummary> summaries) {
        if (summaries == null) {
            return Collections.emptyList();
        }
        return summaries.stream()
                .map(this::toBusinessHourDto)
                .collect(Collectors.toList());
    }

    default BusinessHourDTO toBusinessHourDto(MerchantProfileQueryResult.BusinessHourSummary summary) {
        if (summary == null) {
            return null;
        }
        BusinessHourDTO dto = new BusinessHourDTO();
        dto.setDayOfWeek(summary.dayOfWeek());
        dto.setOpenTime(summary.openTime());
        dto.setCloseTime(summary.closeTime());
        return dto;
    }
}
