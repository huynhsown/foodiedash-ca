package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemOptionValueJpaMapper {
    CartItemOptionValue toDomain(CartItemOptionValueJpaEntity jpaEntity);
    CartItemOptionValueJpaEntity toJpaEntity(CartItemOptionValue domain);
}
