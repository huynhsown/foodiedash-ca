package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartJpaMapper {
    Cart toDomain(CartJpaEntity jpaEntity);
    CartJpaEntity toJpaEntity(Cart domain);
}
