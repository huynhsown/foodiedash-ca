package com.ute.foodiedash.interfaces.rest.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartCountResponseDTO {
    private Integer totalItems;
}
