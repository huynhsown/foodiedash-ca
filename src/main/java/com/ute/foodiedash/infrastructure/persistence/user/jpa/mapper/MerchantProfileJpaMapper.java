package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.MerchantProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantProfileJpaMapper {
    @Mapping(target = "userId", source = "user.id")
    MerchantProfile toDomain(MerchantProfileJpaEntity jpaEntity);

    @Mapping(target = "user", ignore = true)
    MerchantProfileJpaEntity toJpaEntity(MerchantProfile merchant);

    default MerchantProfileJpaEntity toJpaEntity(MerchantProfile merchant, UserJpaEntity user) {
        MerchantProfileJpaEntity jpaEntity = toJpaEntity(merchant);
        jpaEntity.setUser(user);
        return jpaEntity;
    }
}
