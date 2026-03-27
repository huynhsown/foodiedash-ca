package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.domain.user.model.DriverProfile;
import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.domain.user.model.UserRole;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerAddressJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CustomerProfileJpaMapper.class,
                MerchantProfileJpaMapper.class,
                DriverProfileJpaMapper.class,
                CustomerAddressJpaMapper.class,
                UserRoleJpaMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserJpaMapper {

    default User toDomain(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        CustomerProfile customerProfile = null;
        if (jpaEntity.getCustomerProfile() != null) {
            customerProfile = CustomerProfile.reconstruct(
                    jpaEntity.getCustomerProfile().getId(),
                    jpaEntity.getId(),
                    jpaEntity.getCustomerProfile().getDateOfBirth(),
                    jpaEntity.getCustomerProfile().getGender(),
                    jpaEntity.getCustomerProfile().getCreatedAt(),
                    jpaEntity.getCustomerProfile().getUpdatedAt(),
                    jpaEntity.getCustomerProfile().getCreatedBy(),
                    jpaEntity.getCustomerProfile().getUpdatedBy(),
                    jpaEntity.getCustomerProfile().getDeletedAt(),
                    jpaEntity.getCustomerProfile().getVersion()
            );
        }

        MerchantProfile merchantProfile = null;
        if (jpaEntity.getMerchantProfile() != null) {
            merchantProfile = MerchantProfile.reconstruct(
                    jpaEntity.getMerchantProfile().getId(),
                    jpaEntity.getId(),
                    jpaEntity.getMerchantProfile().getBusinessName(),
                    jpaEntity.getMerchantProfile().getBusinessLicense(),
                    jpaEntity.getMerchantProfile().getTaxCode(),
                    jpaEntity.getMerchantProfile().getBankName(),
                    jpaEntity.getMerchantProfile().getBankAccount(),
                    jpaEntity.getMerchantProfile().getBankHolderName(),
                    jpaEntity.getMerchantProfile().getContactEmail(),
                    jpaEntity.getMerchantProfile().getContactPhone(),
                    jpaEntity.getMerchantProfile().getVerificationStatus(),
                    jpaEntity.getMerchantProfile().getCreatedAt(),
                    jpaEntity.getMerchantProfile().getUpdatedAt(),
                    jpaEntity.getMerchantProfile().getCreatedBy(),
                    jpaEntity.getMerchantProfile().getUpdatedBy(),
                    jpaEntity.getMerchantProfile().getDeletedAt(),
                    jpaEntity.getMerchantProfile().getVersion()
            );
        }

        DriverProfile driverProfile = null;
        if (jpaEntity.getDriverProfile() != null) {
            VehicleType vehicleType = null;
            if (jpaEntity.getDriverProfile().getVehicleType() != null) {
                try {
                    vehicleType = VehicleType.valueOf(jpaEntity.getDriverProfile().getVehicleType());
                } catch (IllegalArgumentException ignored) {
                    vehicleType = null;
                }
            }

            DriverVerificationStatus verificationStatus = null;
            if (jpaEntity.getDriverProfile().getVerificationStatus() != null) {
                try {
                    verificationStatus = DriverVerificationStatus.valueOf(
                            jpaEntity.getDriverProfile().getVerificationStatus().name()
                    );
                } catch (IllegalArgumentException ignored) {
                    verificationStatus = null;
                }
            }

            driverProfile = DriverProfile.reconstruct(
                    jpaEntity.getDriverProfile().getId(),
                    jpaEntity.getId(),
                    jpaEntity.getDriverProfile().getIdCardNumber(),
                    jpaEntity.getDriverProfile().getIdCardFrontUrl(),
                    jpaEntity.getDriverProfile().getIdCardBackUrl(),
                    jpaEntity.getDriverProfile().getLicenseNumber(),
                    vehicleType,
                    jpaEntity.getDriverProfile().getVehiclePlate(),
                    jpaEntity.getDriverProfile().getDriverLicenseUrl(),
                    jpaEntity.getDriverProfile().getBankName(),
                    jpaEntity.getDriverProfile().getBankAccount(),
                    jpaEntity.getDriverProfile().getBankHolderName(),
                    jpaEntity.getDriverProfile().getCurrentLat(),
                    jpaEntity.getDriverProfile().getCurrentLng(),
                    Boolean.TRUE.equals(jpaEntity.getDriverProfile().getIsOnline()),
                    verificationStatus,
                    jpaEntity.getDriverProfile().getCreatedAt(),
                    jpaEntity.getDriverProfile().getUpdatedAt(),
                    jpaEntity.getDriverProfile().getCreatedBy(),
                    jpaEntity.getDriverProfile().getUpdatedBy(),
                    jpaEntity.getDriverProfile().getDeletedAt(),
                    jpaEntity.getDriverProfile().getVersion()
            );
        }

        List<CustomerAddress> addresses = new ArrayList<>();
        if (jpaEntity.getAddresses() != null) {
            for (CustomerAddressJpaEntity a : jpaEntity.getAddresses()) {
                if (a == null) continue;
                addresses.add(CustomerAddress.reconstruct(
                        a.getId(),
                        jpaEntity.getId(),
                        a.getLabel(),
                        a.getAddress(),
                        a.getLat(),
                        a.getLng(),
                        a.getReceiverName(),
                        a.getReceiverPhone(),
                        a.getNote(),
                        Boolean.TRUE.equals(a.getDefaultAddress()),
                        a.getCreatedAt(),
                        a.getUpdatedAt(),
                        a.getCreatedBy(),
                        a.getUpdatedBy(),
                        a.getDeletedAt(),
                        a.getVersion()
                ));
            }
        }

        List<UserRole> roles = new ArrayList<>();
        if (jpaEntity.getRoles() != null) {
            for (UserRoleJpaEntity r : jpaEntity.getRoles()) {
                if (r == null || r.getId() == null) continue;
                roles.add(UserRole.reconstruct(jpaEntity.getId(), r.getId().getRoleName()));
            }
        }

        return User.reconstruct(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getPhone(),
                jpaEntity.getPassword(),
                jpaEntity.getFullName(),
                jpaEntity.getAvatarUrl(),
                jpaEntity.getStatus(),
                customerProfile,
                merchantProfile,
                driverProfile,
                addresses,
                roles,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    UserJpaEntity toJpaEntity(User domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateJpaEntity(User domain, @MappingTarget UserJpaEntity jpaEntity);

    @AfterMapping
    default void setUserReferences(@MappingTarget UserJpaEntity jpaEntity) {
        if (jpaEntity.getCustomerProfile() != null) {
            jpaEntity.getCustomerProfile().setUser(jpaEntity);
        }
        if (jpaEntity.getMerchantProfile() != null) {
            jpaEntity.getMerchantProfile().setUser(jpaEntity);
        }
        if (jpaEntity.getDriverProfile() != null) {
            jpaEntity.getDriverProfile().setUser(jpaEntity);
        }
        if (jpaEntity.getAddresses() != null) {
            jpaEntity.getAddresses().forEach(address -> address.setUser(jpaEntity));
        }
        if (jpaEntity.getRoles() != null) {
            jpaEntity.getRoles().forEach(role -> {
                role.setUser(jpaEntity);
            });
        }
    }
}
