package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.query.CartCountQueryResult;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCartCountUseCase {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public CartCountQueryResult execute(Long userId) {
        List<Cart> carts = cartRepository
            .findByUserIdAndStatusAndDeletedAtIsNull(userId, CartStatus.ACTIVE);
        
        List<Long> cartIds = carts.stream()
            .map(Cart::getId)
            .toList();

        Integer itemCount = cartItemRepository.sumQuantityByCartIds(cartIds);
        if (itemCount == null) {
            itemCount = 0;
        }

        return new CartCountQueryResult(itemCount);
    }
}
