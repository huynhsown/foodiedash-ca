package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerProfileJpaMapper {

    @Mapping(target = "userId", source = "user.id")
    CustomerProfile toDomain(CustomerProfileJpaEntity jpaEntity);

    @Mapping(target = "user", ignore = true)
    CustomerProfileJpaEntity toJpaEntity(CustomerProfile domain);

    default CustomerProfileJpaEntity toJpaEntity(CustomerProfile domain, UserJpaEntity user) {
        CustomerProfileJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }

}
