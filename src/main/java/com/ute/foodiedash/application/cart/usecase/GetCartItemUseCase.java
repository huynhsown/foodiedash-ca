package com.ute.foodiedash.application.cart.usecase;

import com.ute.foodiedash.application.cart.query.CartItemOptionQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemOptionValueQueryResult;
import com.ute.foodiedash.application.cart.query.CartItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemOptionValueRepository;
import com.ute.foodiedash.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetCartItemUseCase {
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartItemOptionValueRepository cartItemOptionValueRepository;

    @Transactional(readOnly = true)
    public CartItemQueryResult execute(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new NotFoundException("Cart item not found"));

        List<CartItemOption> cartItemOptions = cartItemOptionRepository
            .findByCartItemIdAndDeletedAtIsNull(cartItemId);

        List<Long> cartItemOptionIds = cartItemOptions.stream()
            .map(CartItemOption::getId)
            .toList();

        List<CartItemOptionValue> cartItemOptionValues = cartItemOptionValueRepository
            .findByCartItemOptionIdInAndDeletedAtIsNull(cartItemOptionIds);

        Map<Long, List<CartItemOptionValue>> optionValuesGroupedByOptionId =
            cartItemOptionValues.stream()
                .collect(Collectors.groupingBy(CartItemOptionValue::getCartItemOptionId));

        List<CartItemOptionQueryResult> optionResults = cartItemOptions.stream()
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
    }
}
