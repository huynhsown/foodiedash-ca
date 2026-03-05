package com.ute.foodiedash.interfaces.rest.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartItemOptionDTO {
    private Long id;
    private Long optionId;
    private String optionName;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;
    private List<CartItemOptionValueDTO> values;
}
