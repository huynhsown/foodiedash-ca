package com.ute.foodiedash.infrastructure.persistence.cart.adapter;

import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionRepository;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartItemOptionJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartItemOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartItemOptionRepositoryAdapter implements CartItemOptionRepository {
    private final CartItemOptionJpaRepository jpaRepository;
    private final CartItemOptionJpaMapper mapper;

    @Override
    public CartItemOption save(CartItemOption cartItemOption) {
        CartItemOptionJpaEntity jpaEntity = mapper.toJpaEntity(cartItemOption);
        CartItemOptionJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<CartItemOption> findByCartItemIdInAndDeletedAtIsNull(List<Long> cartItemIds) {
        return jpaRepository.findByCartItemIdInAndDeletedAtIsNull(cartItemIds).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CartItemOption> findByCartItemIdAndDeletedAtIsNull(Long cartItemId) {
        return jpaRepository.findByCartItemIdAndDeletedAtIsNull(cartItemId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
