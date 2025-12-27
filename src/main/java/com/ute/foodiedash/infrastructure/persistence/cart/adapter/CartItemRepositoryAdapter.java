package com.ute.foodiedash.infrastructure.persistence.cart.adapter;

import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartItemJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartItemRepositoryAdapter implements CartItemRepository {
    private final CartItemJpaRepository jpaRepository;
    private final CartItemJpaMapper mapper;

    @Override
    public CartItem save(CartItem cartItem) {
        CartItemJpaEntity jpaEntity = mapper.toJpaEntity(cartItem);
        CartItemJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return jpaRepository.findByCartId(cartId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> findByCartIdAndDeletedAtIsNull(Long cartId) {
        return jpaRepository.findByCartIdAndDeletedAtIsNull(cartId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CartItem> findByCartItemIdIn(List<Long> cartItemIds) {
        return jpaRepository.findByCartItemIdIn(cartItemIds).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Integer sumQuantityByCartIds(List<Long> cartIds) {
        return jpaRepository.sumQuantityByCartIds(cartIds);
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
