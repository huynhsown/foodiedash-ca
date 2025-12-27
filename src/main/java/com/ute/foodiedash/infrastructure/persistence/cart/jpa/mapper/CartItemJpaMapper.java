package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CartItemOptionJpaMapper.class})
public interface CartItemJpaMapper {
    @Mapping(target = "cartId", source = "cart.id")
    CartItem toDomain(CartItemJpaEntity jpaEntity);
    @Mapping(target = "cart", ignore = true)
    CartItemJpaEntity toJpaEntity(CartItem domain);

    @AfterMapping
    default void setCartItemReferences(@MappingTarget CartItemJpaEntity jpaEntity) {
        if (!jpaEntity.getOptions().isEmpty()) {
            for (var item : jpaEntity.getOptions()) {
                item.setCartItem(jpaEntity);
            }
        }
    }
}
