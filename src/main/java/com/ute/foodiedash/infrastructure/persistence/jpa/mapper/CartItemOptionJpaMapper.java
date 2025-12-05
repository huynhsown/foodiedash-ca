package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.CartItemOptionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemOptionJpaMapper {
    CartItemOption toDomain(CartItemOptionJpaEntity jpaEntity);
    CartItemOptionJpaEntity toJpaEntity(CartItemOption domain);
}
