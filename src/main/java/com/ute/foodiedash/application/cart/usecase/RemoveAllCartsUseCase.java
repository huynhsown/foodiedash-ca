package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.cart.enums.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveAllCartsUseCase {
    private final CartRepository cartRepository;

    @Transactional
    public void execute(Long userId) {
        List<Cart> carts = cartRepository
            .findByUserIdAndStatusAndDeletedAtIsNull(userId, CartStatus.ACTIVE);
        
        if (!carts.isEmpty()) {
            cartRepository.deleteAll(carts);
        }
    }
}
