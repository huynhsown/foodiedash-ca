package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreCartItemUseCase {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void execute(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new BadRequestException("Cart not found"));
        cart.restoreItemById(cartItemId);
        cartRepository.save(cart);

    }
}
