package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerAddressJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerAddressJpaMapper {
    @Mapping(target = "userId", source = "user.id")
    CustomerAddress toDomain(CustomerAddressJpaEntity jpaEntity);

    @Mapping(target = "user", ignore = true)
    CustomerAddressJpaEntity toJpaEntity(CustomerAddress domain);

    default CustomerAddressJpaEntity toJpaEntity(CustomerAddress domain, UserJpaEntity user) {
        CustomerAddressJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }
}
