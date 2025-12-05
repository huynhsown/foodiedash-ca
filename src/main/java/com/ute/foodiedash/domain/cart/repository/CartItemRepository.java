package com.ute.foodiedash.domain.cart.repository;

import com.ute.foodiedash.domain.cart.model.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);
    Optional<CartItem> findById(Long id);
    List<CartItem> findByCartId(Long cartId);
    List<CartItem> findByCartIdAndDeletedAtIsNull(Long cartId);
    List<CartItem> findByCartItemIdIn(List<Long> cartItemIds);
    Integer sumQuantityByCartIds(List<Long> cartIds);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
