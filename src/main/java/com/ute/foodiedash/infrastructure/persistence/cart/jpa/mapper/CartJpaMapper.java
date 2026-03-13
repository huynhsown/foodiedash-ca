package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                CartItemJpaMapper.class
        }
)
public interface CartJpaMapper {
    Cart toDomain(CartJpaEntity jpaEntity);

    CartJpaEntity toJpaEntity(Cart domain);

    @AfterMapping
    default void setCartReferences(@MappingTarget CartJpaEntity jpaEntity) {
        if (!jpaEntity.getItems().isEmpty()) {
            for (var item : jpaEntity.getItems()) {
                item.setCart(jpaEntity);
            }
        }
    }
}
