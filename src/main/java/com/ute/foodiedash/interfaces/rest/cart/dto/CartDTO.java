package com.ute.foodiedash.interfaces.rest.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private String status;
    private LocalDateTime expiresAt;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private List<CartItemDTO> items;
}
