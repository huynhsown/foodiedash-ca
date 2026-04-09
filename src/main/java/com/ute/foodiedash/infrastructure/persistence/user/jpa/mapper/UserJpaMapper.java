package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.domain.user.model.DriverProfile;
import com.ute.foodiedash.domain.user.model.MerchantRestaurant;
import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.domain.user.model.RestaurantDevice;
import com.ute.foodiedash.domain.user.model.UserRole;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerAddressJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.DriverProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.MerchantProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.MerchantRestaurantJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RestaurantDeviceJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public abstract class UserJpaMapper {

    @Autowired
    protected CustomerProfileJpaMapper customerProfileJpaMapper;
    @Autowired
    protected MerchantProfileJpaMapper merchantProfileJpaMapper;
    @Autowired
    protected DriverProfileJpaMapper driverProfileJpaMapper;
    @Autowired
    protected CustomerAddressJpaMapper customerAddressJpaMapper;
    @Autowired
    protected UserRoleJpaMapper userRoleJpaMapper;

    public User toDomainWithDriverProfile(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        DriverProfile driverProfile = null;
        if (jpaEntity.getDriverProfile() != null) {
            driverProfile = mapDriverProfile(jpaEntity);
        }

        List<UserRole> roles = new ArrayList<>();
        if (jpaEntity.getRoles() != null) {
            for (UserRoleJpaEntity r : jpaEntity.getRoles()) {
                if (r == null || r.getId() == null) continue;
                roles.add(UserRole.reconstruct(jpaEntity.getId(), r.getId().getRoleName()));
            }
        }

        return User.reconstruct(
                jpaEntity.getId(), jpaEntity.getEmail(), jpaEntity.getPhone(),
                jpaEntity.getPassword(), jpaEntity.getFullName(), jpaEntity.getAvatarUrl(),
                jpaEntity.getStatus(),
                null, null, driverProfile,
                null, null, null, roles,
                jpaEntity.getCreatedAt(), jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(), jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(), jpaEntity.getVersion()
        );
    }

    private DriverProfile mapDriverProfile(UserJpaEntity jpaEntity) {
        if (jpaEntity.getDriverProfile() == null) return null;

        VehicleType vehicleType = null;
        if (jpaEntity.getDriverProfile().getVehicleType() != null) {
            try {
                vehicleType = jpaEntity.getDriverProfile().getVehicleType();
            } catch (IllegalArgumentException ignored) {
                vehicleType = null;
            }
        }

        DriverVerificationStatus verificationStatus = null;
        if (jpaEntity.getDriverProfile().getDriverVerificationStatus() != null) {
            try {
                verificationStatus = DriverVerificationStatus.valueOf(
                        jpaEntity.getDriverProfile().getDriverVerificationStatus().name()
                );
            } catch (IllegalArgumentException ignored) {
                verificationStatus = null;
            }
        }

        return DriverProfile.reconstruct(
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

    public User toDomain(UserJpaEntity jpaEntity) {
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

        List<MerchantRestaurant> merchantRestaurants = new ArrayList<>();
        List<RestaurantDevice> restaurantDevices = new ArrayList<>();

        MerchantProfile merchantProfile = null;
        if (jpaEntity.getMerchantProfile() != null) {
            if (jpaEntity.getMerchantRestaurants() != null) {
                for (MerchantRestaurantJpaEntity mr : jpaEntity.getMerchantRestaurants()) {
                    if (mr == null) continue;
                    merchantRestaurants.add(MerchantRestaurant.reconstruct(
                            mr.getId(),
                            jpaEntity.getId(),
                            mr.getRestaurantId(),
                            mr.getCreatedAt(),
                            mr.getUpdatedAt(),
                            mr.getCreatedBy(),
                            mr.getUpdatedBy(),
                            mr.getDeletedAt(),
                            mr.getVersion()
                    ));
                }
            }

            if (jpaEntity.getRestaurantDevices() != null) {
                for (RestaurantDeviceJpaEntity rd : jpaEntity.getRestaurantDevices()) {
                    if (rd == null) continue;
                    restaurantDevices.add(RestaurantDevice.reconstruct(
                            rd.getId(),
                            jpaEntity.getId(),
                            rd.getRestaurantId(),
                            rd.getDeviceName(),
                            rd.getLastLoginAt(),
                            rd.getCreatedAt(),
                            rd.getUpdatedAt(),
                            rd.getCreatedBy(),
                            rd.getUpdatedBy(),
                            rd.getDeletedAt(),
                            rd.getVersion()
                    ));
                }
            }

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
                    jpaEntity.getMerchantProfile().getMerchantVerificationStatus(),
                    jpaEntity.getMerchantProfile().getCreatedAt(),
                    jpaEntity.getMerchantProfile().getUpdatedAt(),
                    jpaEntity.getMerchantProfile().getCreatedBy(),
                    jpaEntity.getMerchantProfile().getUpdatedBy(),
                    jpaEntity.getMerchantProfile().getDeletedAt(),
                    jpaEntity.getMerchantProfile().getVersion()
            );
        }

        DriverProfile driverProfile = mapDriverProfile(jpaEntity);

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
                merchantRestaurants,
                restaurantDevices,
                roles,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    public abstract UserJpaEntity toJpaEntity(User domain);

    public void updateJpaEntity(User domain, @MappingTarget UserJpaEntity jpaEntity) {
        jpaEntity.setEmail(domain.getEmail());
        jpaEntity.setPhone(domain.getPhone());
        jpaEntity.setPassword(domain.getPassword());
        jpaEntity.setFullName(domain.getFullName());
        jpaEntity.setAvatarUrl(domain.getAvatarUrl());
        jpaEntity.setStatus(domain.getStatus());
        mergeCustomerProfile(jpaEntity, domain);
        mergeMerchantProfile(jpaEntity, domain);
        mergeDriverProfile(jpaEntity, domain);
        mergeAddresses(jpaEntity, domain);
        mergeRoles(jpaEntity, domain);
        mergeMerchantRestaurants(jpaEntity, domain);
        mergeRestaurantDevices(jpaEntity, domain);
        jpaEntity.setDeletedAt(domain.getDeletedAt());
        jpaEntity.setVersion(domain.getVersion());
    }

    private void mergeCustomerProfile(UserJpaEntity e, User domain) {
        if (domain.getCustomerProfile() != null) {
            if (e.getCustomerProfile() != null) {
                customerProfileJpaMapper.updateEntity(e.getCustomerProfile(), domain.getCustomerProfile());
            } else {
                CustomerProfileJpaEntity jpa = customerProfileJpaMapper.toJpaEntity(domain.getCustomerProfile());
                jpa.setUser(e);
                e.setCustomerProfile(jpa);
            }
        } else {
            e.setCustomerProfile(null);
        }
    }

    private void mergeMerchantProfile(UserJpaEntity e, User domain) {
        if (domain.getMerchantProfile() != null) {
            if (e.getMerchantProfile() != null) {
                merchantProfileJpaMapper.updateEntity(e.getMerchantProfile(), domain.getMerchantProfile());
            } else {
                MerchantProfileJpaEntity jpa = merchantProfileJpaMapper.toJpaEntity(domain.getMerchantProfile());
                jpa.setUser(e);
                e.setMerchantProfile(jpa);
            }
        } else {
            e.setMerchantProfile(null);
        }
    }

    private void mergeDriverProfile(UserJpaEntity e, User domain) {
        if (domain.getDriverProfile() != null) {
            if (e.getDriverProfile() != null) {
                driverProfileJpaMapper.updateEntity(e.getDriverProfile(), domain.getDriverProfile());
            } else {
                DriverProfileJpaEntity jpa = driverProfileJpaMapper.toJpaEntity(domain.getDriverProfile());
                jpa.setUser(e);
                e.setDriverProfile(jpa);
            }
        } else {
            e.setDriverProfile(null);
        }
    }

    private void mergeAddresses(UserJpaEntity e, User domain) {
        if (domain.getAddresses() == null) {
            e.getAddresses().clear();
            return;
        }

        Set<Long> domainIds = domain.getAddresses().stream()
                .map(CustomerAddress::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getAddresses().removeIf(a -> !domainIds.contains(a.getId()));

        Map<Long, CustomerAddressJpaEntity> existingById = e.getAddresses().stream()
                .collect(Collectors.toMap(CustomerAddressJpaEntity::getId, Function.identity()));

        for (CustomerAddress domainAddr : domain.getAddresses()) {
            if (domainAddr.getId() != null && existingById.containsKey(domainAddr.getId())) {
                customerAddressJpaMapper.updateEntity(existingById.get(domainAddr.getId()), domainAddr);
            } else {
                CustomerAddressJpaEntity jpaAddr = customerAddressJpaMapper.toJpaEntity(domainAddr);
                jpaAddr.setUser(e);
                e.getAddresses().add(jpaAddr);
            }
        }
    }

    private void mergeRoles(UserJpaEntity e, User domain) {
        if (domain.getRoles() == null) {
            e.getRoles().clear();
            return;
        }

        Set<RoleName> domainRoleNames = domain.getRoles().stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toSet());

        e.getRoles().removeIf(r -> !domainRoleNames.contains(r.getId().getRoleName()));

        Set<RoleName> existingRoleNames = e.getRoles().stream()
                .map(r -> r.getId().getRoleName())
                .collect(Collectors.toSet());

        for (UserRole domainRole : domain.getRoles()) {
            if (!existingRoleNames.contains(domainRole.getRoleName())) {
                UserRoleJpaEntity jpaRole = userRoleJpaMapper.toJpaEntity(domainRole, e);
                e.getRoles().add(jpaRole);
            }
        }
    }

    private void mergeMerchantRestaurants(UserJpaEntity e, User domain) {
        if (domain.getMerchantRestaurants() == null) {
            e.getMerchantRestaurants().clear();
            return;
        }

        Set<Long> domainIds = domain.getMerchantRestaurants().stream()
                .map(MerchantRestaurant::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getMerchantRestaurants().removeIf(mr -> !domainIds.contains(mr.getId()));

        Map<Long, MerchantRestaurantJpaEntity> existingById = e.getMerchantRestaurants().stream()
                .collect(Collectors.toMap(MerchantRestaurantJpaEntity::getId, Function.identity()));

        for (MerchantRestaurant domainMr : domain.getMerchantRestaurants()) {
            if (domainMr.getId() != null && existingById.containsKey(domainMr.getId())) {
                MerchantRestaurantJpaEntity existing = existingById.get(domainMr.getId());
                existing.setRestaurantId(domainMr.getRestaurantId());
                existing.setDeletedAt(domainMr.getDeletedAt());
                existing.setVersion(domainMr.getVersion());
            } else {
                MerchantRestaurantJpaEntity jpaMr = new MerchantRestaurantJpaEntity();
                jpaMr.setRestaurantId(domainMr.getRestaurantId());
                jpaMr.setUser(e);
                e.getMerchantRestaurants().add(jpaMr);
            }
        }
    }

    private void mergeRestaurantDevices(UserJpaEntity e, User domain) {
        if (domain.getRestaurantDevices() == null) {
            e.getRestaurantDevices().clear();
            return;
        }

        Set<Long> domainIds = domain.getRestaurantDevices().stream()
                .map(RestaurantDevice::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getRestaurantDevices().removeIf(rd -> !domainIds.contains(rd.getId()));

        Map<Long, RestaurantDeviceJpaEntity> existingById = e.getRestaurantDevices().stream()
                .collect(Collectors.toMap(RestaurantDeviceJpaEntity::getId, Function.identity()));

        for (RestaurantDevice domainRd : domain.getRestaurantDevices()) {
            if (domainRd.getId() != null && existingById.containsKey(domainRd.getId())) {
                RestaurantDeviceJpaEntity existing = existingById.get(domainRd.getId());
                existing.setRestaurantId(domainRd.getRestaurantId());
                existing.setDeviceName(domainRd.getDeviceName());
                existing.setLastLoginAt(domainRd.getLastLoginAt());
                existing.setDeletedAt(domainRd.getDeletedAt());
                existing.setVersion(domainRd.getVersion());
            } else {
                RestaurantDeviceJpaEntity jpaRd = new RestaurantDeviceJpaEntity();
                jpaRd.setRestaurantId(domainRd.getRestaurantId());
                jpaRd.setDeviceName(domainRd.getDeviceName());
                jpaRd.setLastLoginAt(domainRd.getLastLoginAt());
                jpaRd.setUser(e);
                e.getRestaurantDevices().add(jpaRd);
            }
        }
    }

    @AfterMapping
    protected void setUserReferences(@MappingTarget UserJpaEntity jpaEntity) {
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
        if (jpaEntity.getMerchantRestaurants() != null) {
            jpaEntity.getMerchantRestaurants().forEach(merchantRestaurant -> merchantRestaurant.setUser(jpaEntity));
        }
        if (jpaEntity.getRestaurantDevices() != null) {
            jpaEntity.getRestaurantDevices().forEach(restaurantDevice -> restaurantDevice.setUser(jpaEntity));
        }
    }
}
