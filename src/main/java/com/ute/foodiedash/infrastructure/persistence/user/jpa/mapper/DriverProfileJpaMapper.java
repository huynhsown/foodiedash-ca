package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.enums.MerchantVerificationStatus;
import com.ute.foodiedash.domain.user.model.DriverProfile;
import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.DriverProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverProfileJpaMapper {

    default DriverProfile toDomain(DriverProfileJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        Long userId = jpaEntity.getUser() != null ? jpaEntity.getUser().getId() : null;

        VehicleType vehicleType = null;
        if (jpaEntity.getVehicleType() != null) {
            try {
                vehicleType = jpaEntity.getVehicleType();
            } catch (IllegalArgumentException ignored) {
                vehicleType = null;
            }
        }

        DriverVerificationStatus verificationStatus = null;
        if (jpaEntity.getDriverVerificationStatus() != null) {
            try {
                verificationStatus = DriverVerificationStatus.valueOf(jpaEntity.getDriverVerificationStatus().name());
            } catch (IllegalArgumentException ignored) {
                verificationStatus = null;
            }
        }

        return DriverProfile.reconstruct(
                jpaEntity.getId(),
                userId,
                jpaEntity.getIdCardNumber(),
                jpaEntity.getIdCardFrontUrl(),
                jpaEntity.getIdCardBackUrl(),
                jpaEntity.getLicenseNumber(),
                vehicleType,
                jpaEntity.getVehiclePlate(),
                jpaEntity.getDriverLicenseUrl(),
                jpaEntity.getBankName(),
                jpaEntity.getBankAccount(),
                jpaEntity.getBankHolderName(),
                jpaEntity.getCurrentLat(),
                jpaEntity.getCurrentLng(),
                Boolean.TRUE.equals(jpaEntity.getIsOnline()),
                verificationStatus,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    DriverProfileJpaEntity toJpaEntity(DriverProfile domain);

    default DriverProfileJpaEntity toJpaEntity(DriverProfile domain, UserJpaEntity user) {
        DriverProfileJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }

    default void updateEntity(@MappingTarget DriverProfileJpaEntity e, DriverProfile domain) {
        e.setIdCardNumber(domain.getIdCardNumber());
        e.setIdCardFrontUrl(domain.getIdCardFrontUrl());
        e.setIdCardBackUrl(domain.getIdCardBackUrl());
        e.setLicenseNumber(domain.getLicenseNumber());
        e.setVehicleType(domain.getVehicleType() != null ? domain.getVehicleType() : null);
        e.setVehiclePlate(domain.getVehiclePlate());
        e.setDriverLicenseUrl(domain.getDriverLicenseUrl());
        e.setBankName(domain.getBankName());
        e.setBankAccount(domain.getBankAccount());
        e.setBankHolderName(domain.getBankHolderName());
        e.setCurrentLat(domain.getCurrentLat());
        e.setCurrentLng(domain.getCurrentLng());
        e.setIsOnline(domain.isOnline());
        e.setDriverVerificationStatus(domain.getDriverVerificationStatus() != null
                ? DriverVerificationStatus.valueOf(domain.getDriverVerificationStatus().name())
                : null);
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
