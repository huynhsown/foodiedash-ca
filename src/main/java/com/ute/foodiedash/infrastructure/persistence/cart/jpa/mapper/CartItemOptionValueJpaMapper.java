package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemOptionValueJpaMapper {
    @Mapping(target = "cartItemOptionId", source = "cartItemOption.id")
    CartItemOptionValue toDomain(CartItemOptionValueJpaEntity jpaEntity);
    @Mapping(target = "cartItemOption", ignore = true)
    CartItemOptionValueJpaEntity toJpaEntity(CartItemOptionValue domain);

    @Mapping(target = "cartItemOption", ignore = true)
    void updateEntity(@MappingTarget CartItemOptionValueJpaEntity entity, CartItemOptionValue domain);
}
