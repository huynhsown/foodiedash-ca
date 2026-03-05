package com.ute.foodiedash.domain.cart.repository;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import java.util.List;
import java.util.Optional;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findById(Long id);
    List<Cart> findByUserIdAndStatusAndDeletedAtIsNull(Long userId, CartStatus status);
    Optional<Cart> findActiveCart(Long userId, Long restaurantId);
    void deleteAll(List<Cart> carts);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
