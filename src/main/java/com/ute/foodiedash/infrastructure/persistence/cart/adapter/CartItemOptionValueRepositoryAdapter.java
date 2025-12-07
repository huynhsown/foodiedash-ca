package com.ute.foodiedash.infrastructure.persistence.cart.adapter;

import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.mapper.CartItemOptionValueJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository.CartItemOptionValueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartItemOptionValueRepositoryAdapter implements CartItemOptionValueRepository {
    private final CartItemOptionValueJpaRepository jpaRepository;
    private final CartItemOptionValueJpaMapper mapper;

    @Override
    public CartItemOptionValue save(CartItemOptionValue cartItemOptionValue) {
        CartItemOptionValueJpaEntity jpaEntity = mapper.toJpaEntity(cartItemOptionValue);
        CartItemOptionValueJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public java.util.Optional<CartItemOptionValue> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<CartItemOptionValue> findByCartItemOptionIdInAndDeletedAtIsNull(List<Long> cartItemOptionIds) {
        return jpaRepository.findByCartItemOptionIdInAndDeletedAtIsNull(cartItemOptionIds).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CartItemOptionValue> findByCartItemOptionIds(List<Long> cartItemOptionIds) {
        return jpaRepository.findByCartItemOptionIds(cartItemOptionIds).stream()
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
