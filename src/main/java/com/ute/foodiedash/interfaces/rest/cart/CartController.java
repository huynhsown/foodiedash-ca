package com.ute.foodiedash.interfaces.rest.cart;

import com.ute.foodiedash.application.cart.command.AddToCartCommand;
import com.ute.foodiedash.application.cart.query.CartCountQueryResult;
import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.application.cart.usecase.AddToCartUseCase;
import com.ute.foodiedash.application.cart.usecase.GetCartByUserIdUseCase;
import com.ute.foodiedash.application.cart.usecase.GetCartCountUseCase;
import com.ute.foodiedash.application.cart.usecase.GetCartUseCase;
import com.ute.foodiedash.application.cart.usecase.RestoreCartUseCase;
import com.ute.foodiedash.application.cart.usecase.SoftDeleteCartUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.cart.dto.AddToCartRequestDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartCountResponseDTO;
import com.ute.foodiedash.interfaces.rest.cart.dto.CartDTO;
import com.ute.foodiedash.interfaces.rest.cart.mapper.CartDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final AddToCartUseCase addToCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final GetCartByUserIdUseCase getCartByUserIdUseCase;
    private final GetCartCountUseCase getCartCountUseCase;
    private final SoftDeleteCartUseCase softDeleteCartUseCase;
    private final RestoreCartUseCase restoreCartUseCase;
    private final CartDtoMapper cartDtoMapper;

    @PostMapping
    public ResponseEntity<CartDTO> addToCart(@RequestBody @Valid AddToCartRequestDTO dto) {
        Long userId = SecurityContextHelper.getCurrentUserId();
        AddToCartCommand command = cartDtoMapper.toCommand(dto);
        CartQueryResult result = addToCartUseCase.execute(userId, command);
        CartDTO response = cartDtoMapper.toDto(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCartByUserId() {
        Long userId = SecurityContextHelper.getCurrentUserId();
        CartQueryResult result = getCartByUserIdUseCase.execute(userId);
        CartDTO response = cartDtoMapper.toDto(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<CartCountResponseDTO> countCartItem() {
        Long userId = SecurityContextHelper.getCurrentUserId();
        CartCountQueryResult result = getCartCountUseCase.execute(userId);
        CartCountResponseDTO response = cartDtoMapper.toCountDto(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        CartQueryResult result = getCartUseCase.execute(id);
        CartDTO response = cartDtoMapper.toDto(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        softDeleteCartUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCart(@PathVariable Long id) {
        restoreCartUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
