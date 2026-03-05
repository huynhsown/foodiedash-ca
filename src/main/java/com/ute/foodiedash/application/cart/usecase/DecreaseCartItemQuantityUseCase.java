package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DecreaseCartItemQuantityUseCase {
    private final CartItemRepository cartItemRepository;
    private final GetCartItemUseCase getCartItemUseCase;

    @Transactional
    public CartItemQueryResult execute(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException("Cart item not found with id " + cartItemId));

        if (!cartItem.canDecrease()) {
            cartItemRepository.softDeleteById(cartItemId);
            return null;
        }

        cartItem.decreaseQuantity();
        cartItemRepository.save(cartItem);

        return getCartItemUseCase.execute(cartItemId);
    }
}
