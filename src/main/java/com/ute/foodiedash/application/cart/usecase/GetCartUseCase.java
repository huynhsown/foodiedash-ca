package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.query.CartItemOptionQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemOptionValueQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.application.cart.query.CartQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetCartUseCase {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartItemOptionValueRepository cartItemOptionValueRepository;

    @Transactional(readOnly = true)
    public CartQueryResult execute(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository
            .findByCartIdAndDeletedAtIsNull(cart.getId());

        List<Long> cartItemIds = cartItems.stream()
            .map(CartItem::getId)
            .toList();

        List<CartItemOption> cartItemOptions = cartItemOptionRepository
            .findByCartItemIdInAndDeletedAtIsNull(cartItemIds);

        List<Long> cartItemOptionIds = cartItemOptions.stream()
            .map(CartItemOption::getId)
            .toList();

        List<CartItemOptionValue> cartItemOptionValues = cartItemOptionValueRepository
            .findByCartItemOptionIdInAndDeletedAtIsNull(cartItemOptionIds);

        Map<Long, List<CartItemOptionValue>> optionValuesGroupedByOptionId =
            cartItemOptionValues.stream()
                .collect(Collectors.groupingBy(CartItemOptionValue::getCartItemOptionId));

        Map<Long, List<CartItemOption>> optionsGroupedByCartItemId =
            cartItemOptions.stream()
                .collect(Collectors.groupingBy(CartItemOption::getCartItemId));

        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        int totalItemQuantity = 0;

        List<CartItemQueryResult> cartItemResults = cartItems.stream()
            .map(cartItem -> {

                List<CartItemOptionQueryResult> optionResults = optionsGroupedByCartItemId
                    .getOrDefault(cartItem.getId(), List.of())
                    .stream()
                    .map(option -> {
                        List<CartItemOptionValueQueryResult> valueResults =
                            optionValuesGroupedByOptionId
                                .getOrDefault(option.getId(), List.of())
                                .stream()
                                .map(value -> new CartItemOptionValueQueryResult(
                                    value.getId(),
                                    value.getOptionValueId(),
                                    value.getOptionValueName(),
                                    value.getQuantity(),
                                    value.getExtraPrice()
                                ))
                                .toList();

                        return new CartItemOptionQueryResult(
                            option.getId(),
                            option.getOptionId(),
                            option.getOptionName(),
                            option.getRequired(),
                            option.getMinValue(),
                            option.getMaxValue(),
                            valueResults
                        );
                    })
                    .toList();

                return new CartItemQueryResult(
                    cartItem.getId(),
                    cartItem.getMenuItemId(),
                    cartItem.getName(),
                    cartItem.getImageUrl(),
                    cartItem.getNotes(),
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice(),
                    cartItem.getTotalPrice(),
                    optionResults
                );
            })
            .toList();

        BigDecimal finalTotalPrice = cartItems.stream()
            .map(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int finalTotalItems = cartItems.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();

        return new CartQueryResult(
            cart.getId(),
            cart.getUserId(),
            cart.getRestaurantId(),
            cart.getStatus().name(),
            cart.getExpiresAt(),
            finalTotalPrice,
            finalTotalItems,
            cartItemResults
        );
    }
}
