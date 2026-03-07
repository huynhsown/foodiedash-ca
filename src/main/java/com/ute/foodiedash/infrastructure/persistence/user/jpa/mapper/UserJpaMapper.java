package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.*;

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

    User toDomain(UserJpaEntity jpaEntity);

    UserJpaEntity toJpaEntity(User domain);

    @Mapping(target = "id", ignore = true)
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
