package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.CartItemJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemJpaMapper {
    CartItem toDomain(CartItemJpaEntity jpaEntity);
    CartItemJpaEntity toJpaEntity(CartItem domain);
}
