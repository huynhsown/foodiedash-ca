package com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CartItemJpaMapper.class})
public abstract class CartJpaMapper {

    @Autowired
    protected CartItemJpaMapper itemMapper;

    public abstract Cart toDomain(CartJpaEntity jpaEntity);

    public abstract CartJpaEntity toJpaEntity(Cart domain);

    public void updateEntity(@MappingTarget CartJpaEntity entity, Cart domain) {
        entity.setUserId(domain.getUserId());
        entity.setRestaurantId(domain.getRestaurantId());
        entity.setStatus(domain.getStatus());
        entity.setExpiresAt(domain.getExpiresAt());
        mergeItems(entity, domain);
    }

    private void mergeItems(CartJpaEntity entity, Cart domain) {
        Map<Long, CartItemJpaEntity> existingMap = entity.getItems().stream()
                .filter(i -> i.getId() != null)
                .collect(Collectors.toMap(CartItemJpaEntity::getId, Function.identity()));

        Set<CartItemJpaEntity> toRemove = new HashSet<>(entity.getItems());

        for (CartItem item : domain.getItems()) {
            if (item.getId() != null && existingMap.containsKey(item.getId())) {
                CartItemJpaEntity existing = existingMap.get(item.getId());
                itemMapper.updateEntity(existing, item);
                toRemove.remove(existing);
            } else {
                CartItemJpaEntity newEntity = itemMapper.toJpaEntity(item);
                newEntity.setCart(entity);
                entity.getItems().add(newEntity);
            }
        }

        toRemove.forEach(i -> i.setCart(null));
        entity.getItems().removeAll(toRemove);
    }

    @AfterMapping
    void setCartReferences(@MappingTarget CartJpaEntity jpaEntity) {
        if (!jpaEntity.getItems().isEmpty()) {
            for (var item : jpaEntity.getItems()) {
                item.setCart(jpaEntity);
            }
        }
    }
}
