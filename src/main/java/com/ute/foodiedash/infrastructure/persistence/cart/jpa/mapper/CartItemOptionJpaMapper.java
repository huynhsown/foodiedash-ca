package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CartItemOptionValueJpaMapper.class})
public abstract class CartItemOptionJpaMapper {

    @Autowired
    protected CartItemOptionValueJpaMapper optionValueMapper;

    @Mapping(target = "cartItemId", source = "cartItem.id")
    public abstract CartItemOption toDomain(CartItemOptionJpaEntity jpaEntity);

    @Mapping(target = "cartItem", ignore = true)
    public abstract CartItemOptionJpaEntity toJpaEntity(CartItemOption domain);

    public void updateEntity(@MappingTarget CartItemOptionJpaEntity entity, CartItemOption domain) {
        entity.setOptionId(domain.getOptionId());
        entity.setOptionName(domain.getOptionName());
        entity.setRequired(domain.getRequired());
        entity.setMinValue(domain.getMinValue());
        entity.setMaxValue(domain.getMaxValue());
        mergeValues(entity, domain);
    }

    private void mergeValues(CartItemOptionJpaEntity entity, CartItemOption domain) {
        Map<Long, CartItemOptionValueJpaEntity> existingMap = entity.getValues().stream()
                .filter(v -> v.getId() != null)
                .collect(Collectors.toMap(CartItemOptionValueJpaEntity::getId, Function.identity()));

        Set<CartItemOptionValueJpaEntity> toRemove = new HashSet<>(entity.getValues());

        for (CartItemOptionValue value : domain.getValues()) {
            if (value.getId() != null && existingMap.containsKey(value.getId())) {
                CartItemOptionValueJpaEntity existing = existingMap.get(value.getId());
                optionValueMapper.updateEntity(existing, value);
                toRemove.remove(existing);
            } else {
                CartItemOptionValueJpaEntity newEntity = optionValueMapper.toJpaEntity(value);
                newEntity.setCartItemOption(entity);
                entity.getValues().add(newEntity);
            }
        }

        toRemove.forEach(v -> v.setCartItemOption(null));
        entity.getValues().removeAll(toRemove);
    }

    @AfterMapping
    protected void setCartItemOptionReferences(@MappingTarget CartItemOptionJpaEntity jpaEntity) {
        if (!jpaEntity.getValues().isEmpty()) {
            for (var item : jpaEntity.getValues()) {
                item.setCartItemOption(jpaEntity);
            }
        }
    }
}
