package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
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

@Mapper(componentModel = "spring", uses = {CartItemOptionJpaMapper.class})
public abstract class CartItemJpaMapper {

    @Autowired
    protected CartItemOptionJpaMapper optionMapper;

    @Mapping(target = "cartId", source = "cart.id")
    public abstract CartItem toDomain(CartItemJpaEntity jpaEntity);

    @Mapping(target = "cart", ignore = true)
    public abstract CartItemJpaEntity toJpaEntity(CartItem domain);

    public void updateEntity(@MappingTarget CartItemJpaEntity entity, CartItem domain) {
        entity.setMenuItemId(domain.getMenuItemId());
        entity.setName(domain.getName());
        entity.setImageUrl(domain.getImageUrl());
        entity.setQuantity(domain.getQuantity());
        entity.setUnitPrice(domain.getUnitPrice());
        entity.setTotalPrice(domain.getTotalPrice());
        entity.setNotes(domain.getNotes());
        mergeOptions(entity, domain);
    }

    private void mergeOptions(CartItemJpaEntity entity, CartItem domain) {
        Map<Long, CartItemOptionJpaEntity> existingMap = entity.getOptions().stream()
                .filter(o -> o.getId() != null)
                .collect(Collectors.toMap(CartItemOptionJpaEntity::getId, Function.identity()));

        Set<CartItemOptionJpaEntity> toRemove = new HashSet<>(entity.getOptions());

        for (CartItemOption option : domain.getOptions()) {
            if (option.getId() != null && existingMap.containsKey(option.getId())) {
                CartItemOptionJpaEntity existing = existingMap.get(option.getId());
                optionMapper.updateEntity(existing, option);
                toRemove.remove(existing);
            } else {
                CartItemOptionJpaEntity newEntity = optionMapper.toJpaEntity(option);
                newEntity.setCartItem(entity);
                entity.getOptions().add(newEntity);
            }
        }

        toRemove.forEach(o -> o.setCartItem(null));
        entity.getOptions().removeAll(toRemove);
    }

    @AfterMapping
    void setCartItemReferences(@MappingTarget CartItemJpaEntity jpaEntity) {
        if (!jpaEntity.getOptions().isEmpty()) {
            for (var item : jpaEntity.getOptions()) {
                item.setCartItem(jpaEntity);
            }
        }
    }
}
