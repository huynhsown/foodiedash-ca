package com.ute.foodiedash.domain.cart.repository;

import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import java.util.List;

public interface CartItemOptionValueRepository {
    CartItemOptionValue save(CartItemOptionValue cartItemOptionValue);
    java.util.Optional<CartItemOptionValue> findById(Long id);
    List<CartItemOptionValue> findByCartItemOptionIdInAndDeletedAtIsNull(List<Long> cartItemOptionIds);
    List<CartItemOptionValue> findByCartItemOptionIds(List<Long> cartItemOptionIds);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
