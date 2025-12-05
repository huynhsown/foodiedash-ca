package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCartByUserIdUseCase {
    private final CartRepository cartRepository;
    private final GetCartUseCase getCartUseCase;

    @Transactional(readOnly = true)
    public CartQueryResult execute(Long userId) {
        List<Cart> activeCarts = cartRepository
            .findByUserIdAndStatusAndDeletedAtIsNull(userId, CartStatus.ACTIVE);

        if (activeCarts.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }

        return getCartUseCase.execute(activeCarts.get(0).getId());
    }
}
