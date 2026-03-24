package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CartItemOptionValueJpaMapper {

    @Mapping(target = "cartItemOptionId", source = "cartItemOption.id")
    public abstract CartItemOptionValue toDomain(CartItemOptionValueJpaEntity jpaEntity);

    @Mapping(target = "cartItemOption", ignore = true)
    public abstract CartItemOptionValueJpaEntity toJpaEntity(CartItemOptionValue domain);

    public void updateEntity(@MappingTarget CartItemOptionValueJpaEntity entity, CartItemOptionValue domain) {
        entity.setOptionValueId(domain.getOptionValueId());
        entity.setOptionValueName(domain.getOptionValueName());
        entity.setQuantity(domain.getQuantity());
        entity.setExtraPrice(domain.getExtraPrice());
    }
}
