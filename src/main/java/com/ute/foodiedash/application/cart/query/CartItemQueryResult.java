package com.ute.foodiedash.application.cart.query;

import java.math.BigDecimal;
import java.util.List;

public record CartItemQueryResult(
    Long id,
    Long menuItemId,
    String name,
    String imageUrl,
    String notes,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    List<CartItemOptionQueryResult> options
) {}
