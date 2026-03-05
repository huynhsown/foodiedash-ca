package com.ute.foodiedash.interfaces.rest.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemOptionValueRequestDTO {
    @NotNull(message = "Option value ID must not be null")
    private Long optionValueId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
