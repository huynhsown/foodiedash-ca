package com.ute.foodiedash.interfaces.rest.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemDTO {
    private Long id;
    private Long menuItemId;
    private String name;
    private String imageUrl;
    private String notes;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private List<CartItemOptionDTO> options;
}
