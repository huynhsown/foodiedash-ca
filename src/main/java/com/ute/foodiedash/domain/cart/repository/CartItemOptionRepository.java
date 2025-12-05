package com.ute.foodiedash.domain.cart.repository;

import com.ute.foodiedash.domain.cart.model.CartItemOption;
import java.util.List;

public interface CartItemOptionRepository {
    CartItemOption save(CartItemOption cartItemOption);
    List<CartItemOption> findByCartItemIdInAndDeletedAtIsNull(List<Long> cartItemIds);
    List<CartItemOption> findByCartItemIdAndDeletedAtIsNull(Long cartItemId);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
