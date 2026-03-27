package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.DriverProfile;
import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.DriverProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
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
                vehicleType = VehicleType.valueOf(jpaEntity.getVehicleType());
            } catch (IllegalArgumentException ignored) {
                vehicleType = null;
            }
        }

        DriverVerificationStatus verificationStatus = null;
        if (jpaEntity.getVerificationStatus() != null) {
            try {
                verificationStatus = DriverVerificationStatus.valueOf(jpaEntity.getVerificationStatus().name());
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
}
