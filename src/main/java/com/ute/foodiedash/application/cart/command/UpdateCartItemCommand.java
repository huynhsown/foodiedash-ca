package com.ute.foodiedash.application.cart.command;

import java.util.List;

public record UpdateCartItemCommand(
    Long cartItemId,
    Integer quantity,
    String notes,
    List<CartItemOptionCommand> options
) {
    public record CartItemOptionCommand(
        Long optionId,
        List<CartItemOptionValueCommand> values
    ) {}

    public record CartItemOptionValueCommand(
        Long optionValueId,
        Integer quantity
    ) {}
}
