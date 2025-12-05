package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.CartJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.CartJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.CartJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepository {
    private final CartJpaRepository jpaRepository;
    private final CartJpaMapper mapper;

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity jpaEntity = mapper.toJpaEntity(cart);
        CartJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Cart> findByUserIdAndStatusAndDeletedAtIsNull(Long userId, CartStatus status) {
        return jpaRepository.findByUserIdAndStatusAndDeletedAtIsNull(userId, status).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Cart> findActiveCart(Long userId, Long restaurantId) {
        return jpaRepository.findActiveCart(userId, restaurantId)
            .map(mapper::toDomain);
    }

    @Override
    public void deleteAll(List<Cart> carts) {
        List<CartJpaEntity> jpaEntities = carts.stream()
            .map(mapper::toJpaEntity)
            .collect(Collectors.toList());
        jpaRepository.deleteAll(jpaEntities);
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
