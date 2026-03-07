package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.DriverProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.DriverProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverProfileJpaMapper {

    @Mapping(target = "userId", source = "user.id")
    DriverProfile toDomain(DriverProfileJpaEntity jpaEntity);


    @Mapping(target = "user", ignore = true)
    DriverProfileJpaEntity toJpaEntity(DriverProfile domain);

    default DriverProfileJpaEntity toJpaEntity(DriverProfile domain, UserJpaEntity user) {
        DriverProfileJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }
}
