package com.ute.foodiedash.application.cart.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartQueryResult(
    Long id,
    Long userId,
    Long restaurantId,
    String status,
    LocalDateTime expiresAt,
    BigDecimal totalPrice,
    Integer totalItems,
    List<CartItemQueryResult> items
) {}
