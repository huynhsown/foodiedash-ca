package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteCartItemUseCase {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void execute(Long id) {
        cartItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cart item not found"));
        cartItemRepository.softDeleteById(id);
    }
}
