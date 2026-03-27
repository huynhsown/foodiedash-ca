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
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartItemController {
    private final SoftDeleteCartItemUseCase softDeleteCartItemUseCase;
    private final RestoreCartItemUseCase restoreCartItemUseCase;
    private final IncreaseCartItemQuantityUseCase increaseCartItemQuantityUseCase;
    private final DecreaseCartItemQuantityUseCase decreaseCartItemQuantityUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final CartDtoMapper cartDtoMapper;

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId) {

        softDeleteCartItemUseCase.execute(cartId, cartItemId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}/items/{cartItemId}/restore")
    public ResponseEntity<Void> restoreCartItem(@PathVariable Long cartId,
                                                @PathVariable Long cartItemId) {
        restoreCartItemUseCase.execute(cartId, cartItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{cartId}/items/{cartItemId}/increase")
    public ResponseEntity<CartItemDTO> increaseCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId) {

        CartItemQueryResult result =
                increaseCartItemQuantityUseCase.execute(cartId, cartItemId);

        CartItemDTO dto = cartDtoMapper.mapCartItem(result);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{cartId}/items/{cartItemId}/decrease")
    public ResponseEntity<CartItemDTO> decreaseCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId
    ) {
        CartItemQueryResult result = decreaseCartItemQuantityUseCase.execute(cartId, cartItemId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        CartItemDTO dto = cartDtoMapper.mapCartItem(result);
        return ResponseEntity.ok(dto);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<CartItemDTO> updateCartItem(
//        @PathVariable Long id,
//        @RequestBody @Valid UpdateCartItemRequestDTO request
//    ) {
//        UpdateCartItemCommand command = cartDtoMapper.toUpdateCommand(request);
//        CartItemQueryResult result = updateCartItemUseCase.execute(id, command);
//        CartItemDTO dto = cartDtoMapper.mapCartItem(result);
//        return ResponseEntity.ok(dto);
//    }
}
