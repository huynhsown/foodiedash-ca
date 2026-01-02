package com.ute.foodiedash.interfaces.rest.cart;

import com.ute.foodiedash.application.cart.usecase.RestoreCartItemOptionValueUseCase;
import com.ute.foodiedash.application.cart.usecase.SoftDeleteCartItemOptionValueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart-item-option-values")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartItemOptionValueController {
    private final SoftDeleteCartItemOptionValueUseCase softDeleteCartItemOptionValueUseCase;
    private final RestoreCartItemOptionValueUseCase restoreCartItemOptionValueUseCase;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItemOptionValue(@PathVariable Long id) {
        softDeleteCartItemOptionValueUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCartItemOptionValue(@PathVariable Long id) {
        restoreCartItemOptionValueUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
