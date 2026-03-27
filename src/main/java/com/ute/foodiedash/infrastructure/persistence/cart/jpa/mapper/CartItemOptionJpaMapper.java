package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CartItemOptionValueJpaMapper.class})
public interface CartItemOptionJpaMapper {
    @Mapping(target = "cartItemId", source = "cartItem.id")
    CartItemOption toDomain(CartItemOptionJpaEntity jpaEntity);
    @Mapping(target = "cartItem", ignore = true)
    CartItemOptionJpaEntity toJpaEntity(CartItemOption domain);

    @AfterMapping
    default void setCartItemOptionReferences(@MappingTarget CartItemOptionJpaEntity jpaEntity) {
        if (!jpaEntity.getValues().isEmpty()) {
            for (var item : jpaEntity.getValues()) {
                item.setCartItemOption(jpaEntity);
            }
        }
    }
}
