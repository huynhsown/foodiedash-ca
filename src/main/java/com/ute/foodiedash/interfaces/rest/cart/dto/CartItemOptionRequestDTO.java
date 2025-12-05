package com.ute.foodiedash.interfaces.rest.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CartItemOptionRequestDTO {
    @NotNull(message = "Option ID must not be null")
    private Long optionId;

    @Valid
    private List<CartItemOptionValueRequestDTO> values;
}
