package com.ute.foodiedash.application.cart.usecase;

import com.ute.common.exception.BadRequestException;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class IncreaseCartItemQuantityUseCase {
    private final CartRepository cartRepository;
    private final GetCartItemUseCase getCartItemUseCase;

    @Transactional
    public CartItemQueryResult execute(Long cartId, Long cartItemId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new BadRequestException("Cart not found")
        );

        cart.increaseItemQuantity(cartItemId);
        cartRepository.save(cart);

        return getCartItemUseCase.execute(cartItemId);
    }
}
