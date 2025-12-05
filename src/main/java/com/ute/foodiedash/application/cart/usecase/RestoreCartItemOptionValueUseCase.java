package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreCartItemOptionValueUseCase {
    private final CartItemOptionValueRepository cartItemOptionValueRepository;

    @Transactional
    public void execute(Long id) {
        cartItemOptionValueRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cart item option value not found"));
        cartItemOptionValueRepository.restoreById(id);
    }
}
