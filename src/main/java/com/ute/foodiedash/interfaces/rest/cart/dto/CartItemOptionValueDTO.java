package com.ute.foodiedash.interfaces.rest.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemOptionValueDTO {
    private Long id;
    private Long optionValueId;
    private String optionValueName;
    private Integer quantity;
    private BigDecimal extraPrice;
}
