package com.ute.foodiedash.interfaces.rest.cart;

import com.ute.foodiedash.application.cart.command.UpdateCartItemCommand;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.application.cart.usecase.DecreaseCartItemQuantityUseCase;
import com.ute.foodiedash.application.cart.usecase.IncreaseCartItemQuantityUseCase;
import com.ute.foodiedash.application.cart.usecase.RestoreCartItemUseCase;
import com.ute.foodiedash.application.cart.usecase.SoftDeleteCartItemUseCase;
import com.ute.foodiedash.application.cart.usecase.UpdateCartItemUseCase;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartItemDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.UpdateCartItemRequestDTO;
import com.ute.foodiedash.interfaces.rest.cart.mapper.CartDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final SoftDeleteCartItemUseCase softDeleteCartItemUseCase;
    private final RestoreCartItemUseCase restoreCartItemUseCase;
    private final IncreaseCartItemQuantityUseCase increaseCartItemQuantityUseCase;
    private final DecreaseCartItemQuantityUseCase decreaseCartItemQuantityUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final CartDtoMapper cartDtoMapper;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        softDeleteCartItemUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCartItem(@PathVariable Long id) {
        restoreCartItemUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/increase")
    public ResponseEntity<CartItemDTO> increaseCartItemQuantity(@PathVariable Long id) {
        CartItemQueryResult result = increaseCartItemQuantityUseCase.execute(id);
        CartItemDTO dto = cartDtoMapper.mapCartItem(result);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/decrease")
    public ResponseEntity<CartItemDTO> decreaseCartItemQuantity(@PathVariable Long id) {
        CartItemQueryResult result = decreaseCartItemQuantityUseCase.execute(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        CartItemDTO dto = cartDtoMapper.mapCartItem(result);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> updateCartItem(
        @PathVariable Long id,
        @RequestBody @Valid UpdateCartItemRequestDTO request
    ) {
        UpdateCartItemCommand command = cartDtoMapper.toUpdateCommand(request);
        CartItemQueryResult result = updateCartItemUseCase.execute(id, command);
        CartItemDTO dto = cartDtoMapper.mapCartItem(result);
        return ResponseEntity.ok(dto);
    }
}
