package com.ute.foodiedash.interfaces.rest.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AddToCartRequestDTO {
    @NotNull(message = "Restaurant ID must not be null")
    private Long restaurantId;

    @NotNull(message = "Menu item ID must not be null")
    private Long menuItemId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Valid
    private List<CartItemOptionRequestDTO> options;
}
